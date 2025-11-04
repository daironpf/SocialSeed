package com.social.seed.controller;

import com.social.seed.model.Post;
import com.social.seed.service.PostService;
import com.social.seed.util.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class handling API endpoints related to posts.
 */
@RestController
@Tag(name = "Post", description = "Post Module")
@RequestMapping("/api/v0.0.1/post")
public class PostController {
    //region Dependencies
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    //endregion

    //region Gets
    /**
     * Retrieves all posts with pagination.
     *
     * @param page Page number for pagination (default is 0).
     * @param size Number of posts per page (default is 10).
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getAllPosts")
    public ResponseEntity<ResponseDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Object> response = postService.getAllPosts(page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Retrieves all posts with pagination.
     *
     * @param userId the id of the user
     * @param userIdRequest the id user to request all his post
     * @param page Page number for pagination (default is 0).
     * @param size Number of posts per page (default is 10).
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getAllPostsByUserId/{userIdRequest}")
    public ResponseEntity<ResponseDTO> getAllPostsByUserId(
            @RequestHeader("userId") String userId,
            @PathVariable String userIdRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Object> response = postService.getAllPostsByUserId(userId, userIdRequest, page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Retrieves the user's post feed with pagination.
     *
     * @param page Page number for pagination (default is 0).
     * @param size Number of posts per page (default is 10).
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getPostsFeed")
    public ResponseEntity<ResponseDTO> getPostsFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Object> response = postService.getPostFeed(page, size);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region CRUD
    /**
     * Retrieves a post by its ID.
     *
     * @param id The ID of the post to retrieve.
     * @return ResponseEntity with a ResponseDTO.
     */
    @GetMapping("/getPostById/{id}")
    public ResponseEntity<ResponseDTO> getPostById(@PathVariable String id) {
        ResponseEntity<Object> response = postService.getPostById(id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Creates a new post.
     *
     * @param userId  The ID of the user creating the post.
     * @param newPost The new post data.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/createPost")
    public ResponseEntity<ResponseDTO> createPost(
            @RequestHeader("userId") String userId,
            @RequestBody Post newPost) {
        ResponseEntity<Object> response = postService.createNewPost(newPost, userId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Updates an existing post.
     *
     * @param userId       The ID of the user making the update request.
     * @param updatedPost  The updated post data.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PutMapping("/updatePost")
    public ResponseEntity<ResponseDTO> updatePost(
            @RequestHeader("userId") String userId,
            @RequestBody Post updatedPost) {
        ResponseEntity<Object> response = postService.updatePost(userId, updatedPost);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deletes a post.
     *
     * @param userId The ID of the user making the delete request.
     * @param id     The ID of the post to delete.
     * @return ResponseEntity with a ResponseDTO.
     */
    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<ResponseDTO> deletePost(
            @RequestHeader("userId") String userId,
            @PathVariable String id) {
        ResponseEntity<Object> response = postService.deletePost(userId, id);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion

    //region Like
    /**
     * Creates a like for a post.
     *
     * @param idUserRequest   The ID of the user making the request.
     * @param idPostToLiked   The ID of the post to be liked.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/createLike/{idPostToLiked}")
    public ResponseEntity<ResponseDTO> createSocialUserlikedPost(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idPostToLiked) {
        ResponseEntity<Object> response = postService.createSocialUserlikedPost(idUserRequest, idPostToLiked);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }

    /**
     * Deletes a like for a post.
     *
     * @param idUserRequest   The ID of the user making the request.
     * @param idPostToLiked   The ID of the post to unlike.
     * @return ResponseEntity with a ResponseDTO.
     */
    @PostMapping("/deleteLike/{idPostToLiked}")
    public ResponseEntity<ResponseDTO> deleteSocialUserlikedPost(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idPostToLiked) {
        ResponseEntity<Object> response = postService.deleteSocialUserlikedPost(idUserRequest, idPostToLiked);
        return ResponseEntity
                .status(response.getStatusCode())
                .body((ResponseDTO) response.getBody());
    }
    //endregion
}
