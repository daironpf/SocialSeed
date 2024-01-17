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
package com.social.seed.validation;

import com.social.seed.model.Post;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Validator class for the {@link com.social.seed.service.PostService}, focusing on validating and ensuring consistency
 * in the operations related to managing { Post within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-17
 */
@Aspect
@Component
public class PostServiceValidator {

    //region Dependencies
    private final ValidationService validationService;
    private final ResponseService responseService;

    @Autowired
    public PostServiceValidator(ValidationService validationService, ResponseService responseService) {
        this.validationService = validationService;
        this.responseService = responseService;
    }
    //endregion

    @Around("execution(* com.social.seed.service.PostService.getPostById(String)) && args(postId)")
    public ResponseEntity<Object> aroundGetPostById(ProceedingJoinPoint joinPoint, String postId) throws Throwable {
        if (!validationService.postExistsById(postId)) {
            return responseService.postNotFoundResponse(postId);
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.PostService.createNewPost(com.social.seed.model.Post, String)) && args(post, userId)")
    @Transactional
    public ResponseEntity<Object> aroundCreateNewPost(ProceedingJoinPoint joinPoint, Post post, String userId) throws Throwable {
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.PostService.updatePost(String, com.social.seed.model.Post)) && args(userId, updatedPost)")
    @Transactional
    public ResponseEntity<Object> aroundUpdatePost(ProceedingJoinPoint joinPoint, String userId, Post updatedPost) throws Throwable {
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        if (!validationService.postExistsById(updatedPost.getId())) {
            return responseService.postNotFoundResponse(updatedPost.getId());
        }

        if (!validationService.userAuthorOfThePostById(userId, updatedPost.getId())) {
            return responseService.isNotPostAuthor();
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.PostService.deletePost(String, String)) && args(userId, postId)")
    @Transactional
    public ResponseEntity<Object> aroundDeletePost(ProceedingJoinPoint joinPoint, String userId, String postId) throws Throwable {
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        if (!validationService.postExistsById(postId)) {
            return responseService.postNotFoundResponse(postId);
        }

        if (!validationService.userAuthorOfThePostById(userId, postId)) {
            return responseService.isNotPostAuthor();
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.PostService.createSocialUserlikedPost(String, String)) && args(userId, postId)")
    @Transactional
    public ResponseEntity<Object> aroundCreateSocialUserLikedPost(ProceedingJoinPoint joinPoint, String userId, String postId) throws Throwable {
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        if (!validationService.postExistsById(postId)) {
            return responseService.postNotFoundResponse(postId);
        }

        if (validationService.userLikedPost(userId, postId)) {
            return responseService.conflictResponseWithMessage("The Post is already liked by this user");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.PostService.deleteSocialUserlikedPost(String, String)) && args(idUserRequest, idPostToLiked)")
    @Transactional
    public ResponseEntity<Object> aroundDeleteSocialUserLikedPost(ProceedingJoinPoint joinPoint, String idUserRequest, String idPostToLiked) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }

        if (!validationService.postExistsById(idPostToLiked)) {
            return responseService.postNotFoundResponse(idPostToLiked);
        }

        if (!validationService.userLikedPost(idUserRequest, idPostToLiked)) {
            return responseService.conflictResponseWithMessage("The Post is Not liked by this user");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }
}