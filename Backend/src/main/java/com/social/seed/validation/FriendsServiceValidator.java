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
 * Validator class for the {@link com.social.seed.service.FriendsService}, focusing on validating and ensuring consistency
 * in the operations related to managing { Friends within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-16
 */
@Aspect
@Component
public class FriendsServiceValidator {
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ResponseService responseService;

    @Around("execution(* com.social.seed.service.FriendsService.createRequestFriendship(String, String)) && args(idUserRequest, idUserToBeFriend)")
    @Transactional
    public ResponseEntity<Object> aroundCreateFriendshipRequest(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToBeFriend) throws Throwable {
        if (idUserRequest.equals(idUserToBeFriend)) {
            return responseService.forbiddenDuplicateSocialUser();
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToBeFriend)) {
            return responseService.userNotFoundResponse(idUserToBeFriend);
        }
        if (validationService.existsFriendRequest(idUserRequest, idUserToBeFriend)) {
            return responseService.conflictResponseWithMessage("The Friend Request already exists");
        }
        if (validationService.existsFriendship(idUserRequest, idUserToBeFriend)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.FriendsService.cancelRequestFriendship(String, String)) && args(requesterUserId, targetUserId)")
    @Transactional
    public ResponseEntity<Object> aroundCancelRequestFriendship(ProceedingJoinPoint joinPoint, String requesterUserId, String targetUserId) throws Throwable {
        if (requesterUserId.equals(targetUserId)) {
            return responseService.forbiddenDuplicateSocialUser();
        }

        if (!validationService.userExistsById(requesterUserId)) {
            return responseService.userNotFoundResponse(requesterUserId);
        }

        if (!validationService.userExistsById(targetUserId)) {
            return responseService.userNotFoundResponse(targetUserId);
        }

        if (!validationService.existsFriendRequest(requesterUserId, targetUserId)) {
            return responseService.notFoundWithMessageResponse("The Friend Request does not exist");
        }

        if (validationService.existsFriendship(requesterUserId, targetUserId)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.FriendsService.acceptedRequestFriendship(String, String)) && args(requesterUserId, targetUserId)")
    @Transactional
    public ResponseEntity<Object> aroundAcceptedRequestFriendship(ProceedingJoinPoint joinPoint, String requesterUserId, String targetUserId) throws Throwable {
        if (requesterUserId.equals(targetUserId)) {
            return responseService.forbiddenDuplicateSocialUser();
        }

        if (!validationService.userExistsById(requesterUserId)) {
            return responseService.userNotFoundResponse(requesterUserId);
        }

        if (!validationService.userExistsById(targetUserId)) {
            return responseService.userNotFoundResponse(targetUserId);
        }

        if (!validationService.existsFriendRequestByUserToAccept(requesterUserId, targetUserId)) {
            return responseService.notFoundWithMessageResponse("The Friendship Request does not exist.");
        }

        if (validationService.existsFriendship(requesterUserId, targetUserId)) {
            return responseService.conflictResponseWithMessage("The Friendship already exists.");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }


    @Around("execution(* com.social.seed.service.FriendsService.deleteFriendship(String, String)) && args(requesterUserId, targetUserId)")
    @Transactional
    public ResponseEntity<Object> aroundDeleteFriendship(ProceedingJoinPoint joinPoint, String requesterUserId, String targetUserId) throws Throwable {
        if (requesterUserId.equals(targetUserId)) {
            return responseService.forbiddenDuplicateSocialUser();
        }

        if (!validationService.userExistsById(requesterUserId)) {
            return responseService.userNotFoundResponse(requesterUserId);
        }

        if (!validationService.userExistsById(targetUserId)) {
            return responseService.userNotFoundResponse(targetUserId);
        }

        if (!validationService.existsFriendship(requesterUserId, targetUserId)) {
            return responseService.conflictResponseWithMessage("There is no friendship relationship between users.");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }
}