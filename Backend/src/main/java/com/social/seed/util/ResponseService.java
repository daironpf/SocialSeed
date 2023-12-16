package com.social.seed.util;

import com.social.seed.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    public ResponseEntity<Object> successResponse(Object data, String message) {
        return new ResponseEntity<>(ResponseDTO.success(data, message), HttpStatus.OK);
    }

    public ResponseEntity<Object> successCreatedResponse(Object data) {
        return new ResponseEntity<>(ResponseDTO.success(data, "Successful"), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> conflictResponseWithMessage(String message) {
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.CONFLICT, null, message, "0.0.1", null);
        return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    public ResponseEntity<Object> forbiddenResponseWithMessage(String message) {
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.FORBIDDEN, null, message, "0.0.1", null);
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }


    public ResponseEntity<Object> userNotFoundResponse(String userId) {
        throw new NotFoundException(String.format("The user with id: %s not found.", userId));
    }

    public ResponseEntity<Object> NotFoundWithMessageResponse(String message) {
        throw new NotFoundException(message);
    }

    public ResponseEntity<Object> alreadyFollow(String userId) {
        return conflictResponseWithMessage(String.format("User %s is already being followed.", userId));
    }

    public ResponseEntity<Object> dontUnFollow(String userId) {
        return conflictResponseWithMessage(String.format("User %s is not being followed.", userId));
    }

    public ResponseEntity<Object> postNotFoundResponse(String postId) {
        throw new NotFoundException(String.format("Post not found with ID: %s", postId));
    }

    public ResponseEntity<Object> isNotPostAuthor() {
        return forbiddenResponseWithMessage("The user making the request is not the author of the post.");
    }

    public ResponseEntity<Object> hashTagNotFoundResponse(String hashTagId) {
        throw new NotFoundException(String.format("HashTag not found with ID: %s", hashTagId));
    }

    // Add other specific error response methods as needed
}
