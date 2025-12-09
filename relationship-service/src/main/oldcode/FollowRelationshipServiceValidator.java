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

import com.social.seed.service.FollowRelationshipService;
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
 * Validator class for the {@link FollowRelationshipService}, focusing on validating and ensuring consistency
 * in the operations related to managing { Follow within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-04-02
 */
@Aspect
@Component
public class FollowRelationshipServiceValidator {
    //region dependencies
    private final ValidationService validationService;
    private final ResponseService responseService;

    @Autowired
    public FollowRelationshipServiceValidator(ValidationService validationService, ResponseService responseService) {
        this.validationService = validationService;
        this.responseService = responseService;
    }
    //endregion

    @Around("execution(* com.social.seed.service.FollowRelationshipService.followSocialUser(String, String)) && args(idUserRequest, idUserToFollow)")
    @Transactional
    public ResponseEntity<Object> aroundCreateFriendshipRequest(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToFollow) throws Throwable {
        if (idUserRequest.equals(idUserToFollow)) {
            return responseService.forbiddenDuplicateSocialUser();
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToFollow)) {
            return responseService.userNotFoundResponse(idUserToFollow);
        }
        // valida si ya el usuario id:idUserRequest está siguiendo al usuario id:idUserToFollow
        if (validationService.isUserBFollowerOfUserA(idUserRequest, idUserToFollow)) {
            return responseService.conflictResponseWithMessage("You are already following the user");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.FollowRelationshipService.unfollowSocialUser(String, String)) && args(idUserRequest, idUserToUnFollow)")
    @Transactional
    public ResponseEntity<Object> aroundUnFollowSocialUser(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToUnFollow) throws Throwable {
        if (idUserRequest.equals(idUserToUnFollow)) {
            return responseService.forbiddenDuplicateSocialUser();
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToUnFollow)) {
            return responseService.userNotFoundResponse(idUserToUnFollow);
        }
        // valida si ya el usuario id:idUserRequest no está siguiendo al usuario id:idUserToUnFollow
        if (!validationService.isUserBFollowerOfUserA(idUserRequest, idUserToUnFollow)) {
            return responseService.conflictResponseWithMessage("Doesn't follow the user yet");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.FollowRelationshipService.getFollowRecommendationsForUserById(String, int, int)) && args(idUserRequest, page, size)")
    @Transactional
    public ResponseEntity<Object> aroundGetFollowRecommendationsForUserById(ProceedingJoinPoint joinPoint, String idUserRequest, int page, int size) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }
}