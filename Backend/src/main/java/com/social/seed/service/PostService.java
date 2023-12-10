package com.social.seed.service;

import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import com.social.seed.repository.PostRepository;
import com.social.seed.repository.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    SocialUserRepository socialUserRepository;

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
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public ResponseEntity<Object> createNewPost(Post post, String userId) {
        Object response;

        if (socialUserRepository.existsById(userId)){
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

            response = postRepository.findById(newPost.getId());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(String.format("The User with the id [ %s ] was not found", userId), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Post> updatePost(String userId, Post updatedPost) {
        Optional<Post> postOptional = postRepository.findById(updatedPost.getId());

        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();

            if (existingPost.getAuthor() != null && existingPost.getAuthor().getAuthor().getId().equals(userId)) {
                postRepository.update(
                        updatedPost.getId(),
                        updatedPost.getContent(),
                        LocalDateTime.now(),
                        updatedPost.getImageUrl()
                );
                Post savedPost = postRepository.findById(updatedPost.getId()).get();
                return ResponseEntity.status(HttpStatus.OK).body(savedPost);

            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public HttpStatus deletePost(String userId, String postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getAuthor() != null && post.getAuthor().getAuthor().getId().equals(userId)) {
                postRepository.deleteById(postId);
                return HttpStatus.OK;
            } else {
                return HttpStatus.CONFLICT;
            }
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }
    //endregion

    //region LIKE
    @Transactional
    public ResponseEntity<Object> createSocialUserlikedPost(String idUserRequest, String idPostToLiked) {
        if (!userExists(idUserRequest)) return userNotFoundResponse(idUserRequest);
        if (!postExists(idPostToLiked)) return postNotFoundResponse(idPostToLiked);
        if (userLikedPost(idUserRequest, idPostToLiked)) return alreadyLikedResponse(idPostToLiked);

        postRepository.createUserByIdLikedPostById(idUserRequest, idPostToLiked, LocalDateTime.now());
        return successResponse(null);
    }

    @Transactional
    public ResponseEntity<Object> deleteSocialUserlikedPost(String idUserRequest, String idPostToLiked) {
        if (!userExists(idUserRequest)) return userNotFoundResponse(idUserRequest);
        if (!postExists(idPostToLiked)) return postNotFoundResponse(idPostToLiked);
        if (!userLikedPost(idUserRequest, idPostToLiked)) return dontLikedResponse(idPostToLiked);

        postRepository.deleteUserByIdLikedPostById(idUserRequest, idPostToLiked);
        return successResponse(null);
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
    //endregion

    //region util response
    private ResponseEntity<Object> userNotFoundResponse(String userId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The User with the id [ %s ] not found", userId));
    }

    private ResponseEntity<Object> postNotFoundResponse(String postId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The Post with the id [ %s ] not found", postId));
    }

    private ResponseEntity<Object> alreadyLikedResponse(String postId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("You are already LIKED the post with id [ %s ]", postId));
    }

    private ResponseEntity<Object> dontLikedResponse(String postId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("The post with id [ %s ] does not have a LIKE from the user", postId));
    }

    private ResponseEntity<Object> successResponse(Object object) {
        return ResponseEntity.status(HttpStatus.OK).body(object);
    }
    //endregion
}
