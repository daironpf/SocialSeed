package com.social.seed.controller;

import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import com.social.seed.service.PostService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    PostService postService;

    //region gets
    @GetMapping("/getAllPosts")
    public ResponseEntity<ResponseDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(OK)
                .body(postService.getAllPosts(page, size)
                        .map(posts -> new ResponseDTO(OK, posts, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", "No posts available")));
    }

    @GetMapping("/getPostsFeed")
    public ResponseEntity<ResponseDTO> getPostsFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(OK)
                .body(postService.getPostFeed(page, size)
                        .map(posts -> new ResponseDTO(OK, posts, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", "No posts available")));
    }
    //endregion

    //region crud
    @GetMapping("/getPostById/{id}")
    public ResponseEntity<ResponseDTO> getPostById(@PathVariable String id) {

        return ResponseEntity.status(OK)
                .body(postService.getPostById(id)
                        .map(post -> new ResponseDTO(OK, post, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", String.format("The Post with the id [ %s ] was not found", id))));
    }

    @PostMapping("/createPost")
    public ResponseEntity<ResponseDTO> createPost(
            @RequestHeader("userId") String userId,
            @RequestBody Post newPost){

        ResponseEntity<Object> responseCreate = postService.createNewPost(newPost, userId);
        HttpStatus status = (HttpStatus) responseCreate.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case CREATED -> new ResponseDTO(status, responseCreate.getBody(), "The Post was Created");
            case CONFLICT, NOT_FOUND -> new ResponseDTO(status, "Error", (String) responseCreate.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PutMapping("/updatePost")
    public ResponseEntity<ResponseDTO> updatePost(
            @RequestHeader("userId") String userId,
            @RequestBody Post updatedPost) {

        ResponseEntity<Post> postResponseEntity  = postService.updatePost(userId, updatedPost);
        HttpStatus status = (HttpStatus) postResponseEntity.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, postResponseEntity.getBody(), String.format("The Post with the id [ %s ] was Updated", updatedPost.getId()));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The Post with the id [ %s ] was not found", updatedPost.getId()));
            case CONFLICT -> new ResponseDTO(status, "Error", String.format("User with the id [ %s ] is not the author of the Post, cannot Modify", userId));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<ResponseDTO> deletePost(
            @RequestHeader("userId") String userId,
            @PathVariable String id) {

        HttpStatus status = postService.deletePost(userId, id);
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", String.format("The Post with the id [ %s ] was Delete", id));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The Post with the id [ %s ] was not found", id));
            case CONFLICT -> new ResponseDTO(status, "Error", String.format("User with the id [ %s ] is not the author of the Post, cannot Delete", userId));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }
    //endregion

    //region LIKE
    @PostMapping("/createLike/{idPostToLiked}")
    public ResponseEntity<ResponseDTO> createSocialUserlikedPost(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idPostToLiked){

        ResponseEntity<Object> response = postService.createSocialUserlikedPost(idUserRequest, idPostToLiked);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", "The LIKE was created Successful ");
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/deleteLike/{idPostToLiked}")
    public ResponseEntity<ResponseDTO> deleteSocialUserlikedPost(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idPostToLiked){

        ResponseEntity<Object> response = postService.deleteSocialUserlikedPost(idUserRequest, idPostToLiked);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", "The LIKE was deleted Successful ");
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    //endregion
}
