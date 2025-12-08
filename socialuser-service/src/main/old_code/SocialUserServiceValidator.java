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

import com.social.seed.model.SocialUser;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Validator class for the {@link com.social.seed.service.SocialUserService}, focusing on validating and ensuring consistency
 * in the operations related to managing { SocialUser within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-17
 */
@Aspect
@Component
public class SocialUserServiceValidator {
    // region Dependencies
    private final ValidationService validationService;
    private final ResponseService responseService;

    @Autowired
    public SocialUserServiceValidator(ValidationService validationService, ResponseService responseService) {
        this.validationService = validationService;
        this.responseService = responseService;
    }
    // endregion

    // region Find by
    @Around("execution(* com.social.seed.service.SocialUserService.getSocialUserByUserName(String)) && args(userName)")
    public ResponseEntity<Object> aroundGetSocialUserByUserName(ProceedingJoinPoint joinPoint, String userName) throws Throwable {
        if (!validationService.userExistByUserName(userName)) {
            return responseService.notFoundWithMessageResponse(String.format("The User with userName: [ %s ] was not found.", userName));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.getSocialUserByEmail(String)) && args(email)")
    public ResponseEntity<Object> aroundGetSocialUserByEmail(ProceedingJoinPoint joinPoint, String email) throws Throwable {
        if (!validationService.userExistByEmail(email)) {
            return responseService.notFoundWithMessageResponse(String.format("The User with email: [ %s ] was not found.", email));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion

    // region CRUD
    @Around("execution(* com.social.seed.service.SocialUserService.getSocialUserById(String)) && args(userId)")
    public ResponseEntity<Object> aroundGetSocialUserById(ProceedingJoinPoint joinPoint, String userId) throws Throwable {
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.createNewSocialUser(com.social.seed.model.SocialUser)) && args(socialUser)")
    public ResponseEntity<Object> aroundCreateNewSocialUser(ProceedingJoinPoint joinPoint, SocialUser socialUser) throws Throwable {
        if (validationService.userExistByUserName(socialUser.getUserName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The userName [ %s ] already exists", socialUser.getUserName()));
        }
        if (validationService.userExistByEmail(socialUser.getEmail())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The Email [ %s ] already exists", socialUser.getEmail()));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.updateSocialUser(String, com.social.seed.model.SocialUser)) && args(userId, newSocialUser)")
    public ResponseEntity<Object> aroundUpdateSocialUser(ProceedingJoinPoint joinPoint, String userId, SocialUser newSocialUser) throws Throwable {
        if (!userId.equals(newSocialUser.getId())) {
            return responseService.forbiddenResponseWithMessage(
                    "The user making the update request is not the owner of this.");
        }
        if (!validationService.userExistsById(userId)) return
                responseService.userNotFoundResponse(userId);

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.deleteSocialUser(String, String)) && args(userId, id)")
    public ResponseEntity<Object> aroundDeleteSocialUser(ProceedingJoinPoint joinPoint, String userId, String id) throws Throwable {
        if (!userId.equals(id)) {
            return responseService.forbiddenResponseWithMessage("The user making the delete request is not the owner of this.");
        }
        if (!validationService.userExistsById(userId)) {
            return responseService.userNotFoundResponse(userId);
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion

    // region Update Operations
    @Around("execution(* com.social.seed.service.SocialUserService.updateSocialUserName(String, String, String)) && args(idUserRequest, idUserToUpdate, newUserName)")
    public ResponseEntity<Object> aroundUpdateSocialUserName(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToUpdate, String newUserName) throws Throwable {
        if (!idUserToUpdate.equals(idUserRequest)) {
            return responseService.forbiddenResponseWithMessage("The user who is requesting the userName change is not the owner of this");
        }
        if (!validationService.userExistsById(idUserToUpdate)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (validationService.userExistByUserName(newUserName)) {
            return responseService.conflictResponseWithMessage(String.format("The userName [ %s ] already exists", newUserName));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.updateSocialUserEmail(String, String, String)) && args(idUserRequest, idUserToUpdate, newEmail)")
    public ResponseEntity<Object> aroundUpdateSocialUserEmail(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToUpdate, String newEmail) throws Throwable {
        if (!idUserToUpdate.equals(idUserRequest)) {
            return responseService.forbiddenResponseWithMessage("The user who is requesting the Email change is not the owner of this");
        }
        if (!validationService.userExistsById(idUserToUpdate)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (validationService.userExistByEmail(newEmail)) {
            return responseService.conflictResponseWithMessage(String.format("The Email [ %s ] already exists", newEmail));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion

    // region Relationship Management
    @Around("execution(* com.social.seed.service.SocialUserService.followSocialUser(String, String)) && args(idUserRequest, idUserToFollow)")
    public ResponseEntity<Object> aroundFollowSocialUser(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToFollow) throws Throwable {
        if (idUserRequest.equals(idUserToFollow)) {
            return responseService.forbiddenResponseWithMessage(
                    "the user to be followed cannot be the same");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToFollow)) {
            return responseService.userNotFoundResponse(idUserToFollow);
        }
        if (validationService.isUserBFollowerOfUserA(idUserRequest, idUserToFollow)) {
            return responseService.conflictResponseWithMessage(String.format("User %s is already being followed.", idUserToFollow));
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.unfollowSocialUser(String, String)) && args(idUserRequest, idUserToUnFollow)")
    public ResponseEntity<Object> aroundUnfollowSocialUser(ProceedingJoinPoint joinPoint, String idUserRequest, String idUserToUnFollow) throws Throwable {
        if (idUserRequest.equals(idUserToUnFollow)) {
            return responseService.forbiddenResponseWithMessage("the user to be unfollowed cannot be the same");
        }
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.userExistsById(idUserToUnFollow)) {
            return responseService.userNotFoundResponse(idUserToUnFollow);
        }
        if (!validationService.isUserBFollowerOfUserA(idUserRequest, idUserToUnFollow)) {
            return responseService.dontUnFollow(idUserToUnFollow);
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion

    // region Vacation Mode Operations
    @Around("execution(* com.social.seed.service.SocialUserService.activateVacationMode(String)) && args(idUserRequest)")
    public ResponseEntity<Object> aroundActivateVacationMode(ProceedingJoinPoint joinPoint, String idUserRequest) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (validationService.isVacationModeActivated(idUserRequest)) {
            return responseService.conflictResponseWithMessage("The Vacation Mode is already Active");
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.deactivateVacationMode(String)) && args(idUserRequest)")
    public ResponseEntity<Object> aroundDeactivateVacationMode(ProceedingJoinPoint joinPoint, String idUserRequest) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.isVacationModeActivated(idUserRequest)) {
            return responseService.conflictResponseWithMessage("The Vacation Mode is already Deactivated");
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion

    // region Activation Operations
    @Around("execution(* com.social.seed.service.SocialUserService.activateSocialUser(String)) && args(idUserRequest)")
    public ResponseEntity<Object> aroundActivateSocialUser(ProceedingJoinPoint joinPoint, String idUserRequest) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (validationService.isSocialUserActivated(idUserRequest)) {
            return responseService.conflictResponseWithMessage("The Social User is already Active");
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserService.deactivateSocialUser(String)) && args(idUserRequest)")
    public ResponseEntity<Object> aroundDeactivateSocialUser(ProceedingJoinPoint joinPoint, String idUserRequest) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }
        if (!validationService.isSocialUserActivated(idUserRequest)) {
            return responseService.conflictResponseWithMessage("The Social User is already Deactivated");
        }

        return (ResponseEntity<Object>) joinPoint.proceed();
    }
    // endregion
}