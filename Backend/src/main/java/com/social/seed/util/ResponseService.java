package com.social.seed.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {
    public ResponseEntity<Object> successResponse(Object data) {
        return new ResponseEntity<>(ResponseDTO.success(data, "Successful"), HttpStatus.OK);
    }

    public ResponseEntity<Object> successResponseWithMessage(Object data, String message) {
        return new ResponseEntity<>(ResponseDTO.success(data, message), HttpStatus.OK);
    }

    public ResponseEntity<Object> successCreatedResponse(Object data) {
        return new ResponseEntity<>(ResponseDTO.success(data, "Created Successful"), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> conflictResponseWithMessage(String message) {
        return new ResponseEntity<>(ResponseDTO.conflict(message), HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> forbiddenResponseWithMessage(String message) {
        return new ResponseEntity<>(ResponseDTO.forbidden(message), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> forbiddenDuplicateSocialUser() {
        return new ResponseEntity<>(ResponseDTO.forbidden("The user cannot be the same."), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> userNotFoundResponse(String userId) {
        return new ResponseEntity<>(ResponseDTO.notFound(String.format("The user with id: [ %s ] was not found.", userId)), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> notFoundWithMessageResponse(String message) {
        return new ResponseEntity<>(ResponseDTO.notFound(message), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> dontUnFollow(String userId) {
        return new ResponseEntity<>(ResponseDTO.conflict(String.format("User %s is not being followed.", userId)), HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> postNotFoundResponse(String postId) {
        return new ResponseEntity<>(ResponseDTO.notFound(String.format("Post not found with ID: %s", postId)), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> isNotPostAuthor() {
        return new ResponseEntity<>(ResponseDTO.forbidden("The user making the request is not the Author of the Post."), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> hashTagNotFoundResponse(String hashTagId) {
        return new ResponseEntity<>(ResponseDTO.notFound(String.format("Hashtag not found with ID: %s", hashTagId)), HttpStatus.NOT_FOUND);
    }
}