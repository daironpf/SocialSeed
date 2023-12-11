package com.social.seed.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    //region Success
    public ResponseEntity<Object> successResponse(Object object) {
        return ResponseEntity.status(HttpStatus.OK).body(object);
    }
    public ResponseEntity<Object> successCreatedResponse(Object object) {
        return ResponseEntity.status(HttpStatus.CREATED).body(object);
    }
    //endregion

    //region NotFound
    public ResponseEntity<Object> NotFoundWithMessageResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);
    }
    public ResponseEntity<Object> userNotFoundResponse(String userId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The User with the id [ %s ] was not found", userId));
    }
    public ResponseEntity<Object> postNotFoundResponse(String postId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The Post with the id [ %s ] was not found", postId));
    }
    public ResponseEntity<Object> hashTagNotFoundResponse(String hashTagId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The HashTag with the id [ %s ] was not found", hashTagId));
    }
    //endregion

    //region CONFLICT
    public ResponseEntity<Object> conflictResponseWithMessage(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(message);
    }
    public ResponseEntity<Object> alreadyLikedResponse(String postId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("You are already LIKED the post with id [ %s ]", postId));
    }
    public ResponseEntity<Object> dontLikedResponse(String postId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("The post with id [ %s ] does not have a LIKE from the user", postId));
    }
    public ResponseEntity<Object> dontUnFollow(String userId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("You not unFollowing the user with id [ %s ]", userId));
    }
    public ResponseEntity<Object> alreadyFollow(String userId) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(String.format("You are already following the user with id [ %s ]", userId));
    }
    //endregion

    //region FORBIDDEN
    public ResponseEntity<Object> isNotPostAuthor() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You are not the author of this post. Access denied.");
    }
    public ResponseEntity<Object> forbiddenResponseWithMessage(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(message);
    }
    //endregion
}
