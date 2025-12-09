/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.repository.PostRepository;
import com.social.seed.util.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Class for operations related to managing the {@link Post} model.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-17
 */
@Service
public class PostService {
    //region Dependencies
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final ResponseService responseService;

    @Autowired
    public PostService(PostRepository postRepository, HashTagRepository hashTagRepository, ResponseService responseService) {
        this.postRepository = postRepository;
        this.hashTagRepository = hashTagRepository;
        this.responseService = responseService;
    }

    //endregion

    //region Gets
    /**
     * Retrieves all posts with pagination.
     *
     * @param page Page number for pagination.
     * @param size Number of posts per page.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageable);

        if (posts.isEmpty()) return responseService.notFoundWithMessageResponse("No posts available.");

        return responseService.successResponse(posts);
    }

    /**
     * Retrieves the user's post feed with pagination.
     *
     * @param page Page number for pagination.
     * @param size Number of posts per page.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getPostFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.getFeed(pageable);

        if (posts.isEmpty()) return responseService.notFoundWithMessageResponse("No posts available.");

        return responseService.successResponse(posts);
    }
    //endregion

    //region CRUD
    /**
     * Retrieves a post by its ID.
     *
     * @param postId The ID of the post.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> getPostById(String postId) {
        Post post = postRepository.findById(postId).get();

        return responseService.successResponse(post);
    }

    /**
     * Creates a new post.
     *
     * @param post   The post to be created.
     * @param userId The ID of the user creating the post.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> createNewPost(Post post, String userId) {
        // Save the new post base properties
        Post newPost = postRepository.save(
                Post.builder()
                        .content(post.getContent())
                        .imageUrl(post.getImageUrl())
                        .isActive(true)
                        .likedCount(0)
                        .build()
        );

        // Extract the hashtags from the content of the post
        String[] hashtags = extractHashtags(post.getContent());

        // Save all the hashtags relationship with the post
        saveAllTheHashtagsRelationshipWithPost(newPost.getId(), hashtags);

        // Create the posted by relationship
        postRepository.createPostedRelationship(
                newPost.getId(),
                userId,
                LocalDateTime.now()
        );

        Optional<Post> post1 = postRepository.findById(newPost.getId());
        if (post1.isEmpty()) return responseService.postNotFoundResponse(newPost.getId());

        return responseService.successCreatedResponse(post1.get());
    }

    /**
     * Updates an existing post.
     *
     * @param userId       The ID of the user making the update request.
     * @param updatedPost  The updated post.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> updatePost(String userId, Post updatedPost) {
        // Update the Post base properties
        postRepository.update(
                updatedPost.getId(),
                updatedPost.getContent(),
                LocalDateTime.now(),
                updatedPost.getImageUrl()
        );

        // Delete all the relationship TAGGED_WITH with the post
        postRepository.deleteAllRelationshipTaggedWithHashTag(updatedPost.getId());

        // Extract the hashtags from the content of the post
        String[] hashtags = extractHashtags(updatedPost.getContent());

        // Save all the hashtags relationship with the post
        saveAllTheHashtagsRelationshipWithPost(updatedPost.getId(), hashtags);

        return responseService.successResponseWithMessage(postRepository.findById(updatedPost.getId()).get(), "Updated");
    }

    /**
     * Deletes a post.
     *
     * @param userId The ID of the user making the delete request.
     * @param postId The ID of the post to be deleted.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    public ResponseEntity<Object> deletePost(String userId, String postId) {
        postRepository.deleteById(postId);

        return responseService.successResponse("The Post was deleted.");
    }
    //endregion

    //region LIKE
    /**
     * Creates a like for a post.
     *
     * @param userId The ID of the user making the request.
     * @param postId The ID of the post to be liked.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> createSocialUserlikedPost(String userId, String postId) {
        postRepository.createUserByIdLikedPostById(userId, postId, LocalDateTime.now());

        return responseService.successResponse("The Post was Liked.");
    }

    /**
     * Deletes a like for a post.
     *
     * @param idUserRequest The ID of the user making the request.
     * @param idPostToLiked The ID of the post to unlike.
     * @return ResponseEntity with the response mapped to a ResponseDTO.
     */
    @Transactional
    public ResponseEntity<Object> deleteSocialUserlikedPost(String idUserRequest, String idPostToLiked) {
        postRepository.deleteUserByIdLikedPostById(idUserRequest, idPostToLiked);

        return responseService.successResponse("The Like was Deleted.");
    }
    //endregion

    //region Util
    /**
     * Extracts hashtags from the content of a post.
     *
     * @param postContent The content of the post.
     * @return Array of hashtags.
     */
    private static String[] extractHashtags(String postContent) {
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(postContent);

        StringBuilder hashtagsBuilder = new StringBuilder();

        while (matcher.find()) {
            hashtagsBuilder.append(matcher.group()).append(" ");
        }

        // Remove last white space if necessary
        String hashtagsString = hashtagsBuilder.toString().trim();

        // Split the result into an array of hashtags
        return hashtagsString.split(" ");
    }

    /**
     * Saves relationships between post and hashtags.
     *
     * @param idPost    The ID of the post.
     * @param hashtags  Array of hashtags.
     */
    private void saveAllTheHashtagsRelationshipWithPost(String idPost, String[] hashtags) {
        for (String hashtagText : hashtags) {
            // Delete the "#" symbol from the text
            String cleanedHashtagText = hashtagText.replace("#", "");

            Optional<HashTag> hashTag = hashTagRepository.findByName(cleanedHashtagText);

            // Validate if the hashtag exists
            if (hashTag.isPresent()) {
                // If it exists, the relationship is created
                postRepository.createRelationshipTaggedWithHashTag(idPost, hashTag.get().getId());
            } else {
                // If the hashtag does not exist, create it and then create the relationship
                HashTag savedNewHashTag = hashTagRepository.save(
                        HashTag.builder()
                                .name(cleanedHashtagText)
                                .socialUserInterestIn(0)
                                .postTaggedIn(0)
                                .build()
                );
                postRepository.createRelationshipTaggedWithHashTag(idPost, savedNewHashTag.getId());
            }
        }
    }
    //endregion

    //region Get
    public ResponseEntity<Object> getAllPostsByUserId(String userId, String userIdRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.getAllPostsByUserId(userIdRequest, pageable);

        if (posts.isEmpty()) return responseService.notFoundWithMessageResponse("No posts available.");

        return responseService.successResponse(posts);
    }
    //endregion
}