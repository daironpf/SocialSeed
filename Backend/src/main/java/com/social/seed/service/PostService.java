package com.social.seed.service;

import com.social.seed.model.Post;
import com.social.seed.repository.PostRepository;
import com.social.seed.repository.SocialUserRepository;
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

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    SocialUserRepository socialUserRepository;
    @Autowired
    ResponseService responseService;

    //region get
    public Optional<Page<Post>> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(postRepository.findAll(pageable));
    }

    public Optional<Page<Post>> getPostFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.of(postRepository.getFeed(pageable));
    }

    //endregion
    //region CRUD
    public ResponseEntity<Object> getPostById(String postId) {
        if (!postExists(postId)) return responseService.postNotFoundResponse(postId);
        return responseService.successResponse(postRepository.findById(postId).get());
    }

    public ResponseEntity<Object> createNewPost(Post post, String userId) {
        if (!userExists(userId)) return responseService.userNotFoundResponse(userId);

        Post newPost = postRepository.save(
                    Post.builder()
                            .content(post.getContent())
                            .imageUrl(post.getImageUrl())
                            .isActive(true)
                            .likedCount(0)
                            .build()
        );

        postRepository.createPostedRelationship(
                    newPost.getId(),
                    userId,
                    LocalDateTime.now()
        );

        return responseService.successCreatedResponse(
                postRepository.findById(newPost.getId()).get()
        );
    }

    public ResponseEntity<Object> updatePost(String userId, Post updatedPost) {
        if (!userExists(userId)) return responseService.userNotFoundResponse(userId);
        if (!postExists(updatedPost.getId())) return responseService.postNotFoundResponse(updatedPost.getId());
        if (!userAuthorOfThePostById(userId, updatedPost.getId())) return responseService.isNotPostAuthor();
        postRepository.update(
            updatedPost.getId(),
            updatedPost.getContent(),
            LocalDateTime.now(),
            updatedPost.getImageUrl()
        );
        Post savedPost = postRepository.findById(updatedPost.getId()).get();
        return responseService.successResponse(savedPost);
    }

    public ResponseEntity<Object> deletePost(String userId, String postId) {
        if (!userExists(userId)) return responseService.userNotFoundResponse(userId);
        if (!postExists(postId)) return responseService.postNotFoundResponse(postId);
        if (!userAuthorOfThePostById(userId, postId)) return responseService.isNotPostAuthor();

        postRepository.deleteById(postId);
        return responseService.successResponse(null);
    }
    //endregion

    //region LIKE
    @Transactional
    public ResponseEntity<Object> createSocialUserlikedPost(String userId, String postId) {
        if (!userExists(userId)) return responseService.userNotFoundResponse(userId);
        if (!postExists(postId)) return responseService.postNotFoundResponse(postId);
        if (userLikedPost(userId, postId)) return responseService.alreadyLikedResponse(postId);

        postRepository.createUserByIdLikedPostById(userId, postId, LocalDateTime.now());
        return responseService.successResponse(null);
    }

    @Transactional
    public ResponseEntity<Object> deleteSocialUserlikedPost(String idUserRequest, String idPostToLiked) {
        if (!userExists(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!postExists(idPostToLiked)) return responseService.postNotFoundResponse(idPostToLiked);
        if (!userLikedPost(idUserRequest, idPostToLiked)) return responseService.dontLikedResponse(idPostToLiked);

        postRepository.deleteUserByIdLikedPostById(idUserRequest, idPostToLiked);
        return responseService.successResponse(null);
    }
    //endregion

    //region util validate
    private boolean userExists(String userId) {
        return socialUserRepository.existsById(userId);
    }

    private boolean postExists(String postId) {
        return postRepository.existsById(postId);
    }

    private boolean userLikedPost(String userId, String postId) {
        return postRepository.isUserByIdLikedPostById(userId, postId);
    }

    private boolean userAuthorOfThePostById(String userId, String postId) {
        return postRepository.isUserAuthorOfThePostById(userId, postId);
    }
    //endregion
}
