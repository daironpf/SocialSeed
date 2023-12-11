package com.social.seed.service;

import com.social.seed.model.Post;
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

@Service
public class PostService {
    //region dependencies
    @Autowired
    PostRepository postRepository;
    @Autowired
    ResponseService responseService;
    @Autowired
    ValidationService validationService;
    //endregion

    //region Gets
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
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);
        return responseService.successResponse(postRepository.findById(postId).get());
    }

    public ResponseEntity<Object> createNewPost(Post post, String userId) {
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);

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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(updatedPost.getId())) return responseService.postNotFoundResponse(updatedPost.getId());
        if (!validationService.userAuthorOfThePostById(userId, updatedPost.getId())) return responseService.isNotPostAuthor();
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
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);
        if (!validationService.userAuthorOfThePostById(userId, postId)) return responseService.isNotPostAuthor();

        postRepository.deleteById(postId);
        return responseService.successResponse(null);
    }
    //endregion

    //region LIKE
    @Transactional
    public ResponseEntity<Object> createSocialUserlikedPost(String userId, String postId) {
        if (!validationService.userExistsById(userId)) return responseService.userNotFoundResponse(userId);
        if (!validationService.postExistsById(postId)) return responseService.postNotFoundResponse(postId);
        if (validationService.userLikedPost(userId, postId)) return responseService.alreadyLikedResponse(postId);

        postRepository.createUserByIdLikedPostById(userId, postId, LocalDateTime.now());
        return responseService.successResponse(null);
    }

    @Transactional
    public ResponseEntity<Object> deleteSocialUserlikedPost(String idUserRequest, String idPostToLiked) {
        if (!validationService.userExistsById(idUserRequest)) return responseService.userNotFoundResponse(idUserRequest);
        if (!validationService.postExistsById(idPostToLiked)) return responseService.postNotFoundResponse(idPostToLiked);
        if (!validationService.userLikedPost(idUserRequest, idPostToLiked)) return responseService.dontLikedResponse(idPostToLiked);

        postRepository.deleteUserByIdLikedPostById(idUserRequest, idPostToLiked);
        return responseService.successResponse(null);
    }
    //endregion
}
