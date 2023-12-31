package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.model.Post;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.repository.PostRepository;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
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

@Service
public class PostService {
    //region Dependencies
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final ResponseService responseService;
    private final ValidationService validationService;

    @Autowired
    public PostService(PostRepository postRepository, HashTagRepository hashTagRepository, ResponseService responseService, ValidationService validationService) {
        this.postRepository = postRepository;
        this.hashTagRepository = hashTagRepository;
        this.responseService = responseService;
        this.validationService = validationService;
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

        if (posts.isEmpty()) return responseService.NotFoundWithMessageResponse("No posts available.");

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

        if (posts.isEmpty()) return responseService.NotFoundWithMessageResponse("No posts available.");

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
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);

        return responseService.successResponse(postRepository.findById(postId).get());
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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);

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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(updatedPost.getId())) return responseService.postNotFoundResponse(updatedPost.getId());
        if (!validationService.userAuthorOfThePostById(userId, updatedPost.getId())) return responseService.isNotPostAuthor();

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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);
        if (!validationService.userAuthorOfThePostById(userId, postId)) return responseService.isNotPostAuthor();

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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);
        if (validationService.userLikedPost(userId, postId)) return responseService.conflictResponseWithMessage("The Post is already liked by this user");

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
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.postExistsById(idPostToLiked)) return responseService.postNotFoundResponse(idPostToLiked);
        if (!validationService.userLikedPost(idUserRequest, idPostToLiked)) return responseService.conflictResponseWithMessage("The Post is Not liked by this user");

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
}