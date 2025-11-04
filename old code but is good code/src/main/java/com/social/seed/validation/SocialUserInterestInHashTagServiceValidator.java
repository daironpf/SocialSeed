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
 * Validator class for the {@link com.social.seed.service.SocialUserInterestInHashTagService}, focusing on validating and ensuring consistency
 * in the operations related to managing { SocialUser Interest in HashTag within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-17
 */
@Aspect
@Component
public class SocialUserInterestInHashTagServiceValidator {
    // region dependencies
    private final ValidationService validationService;
    private final ResponseService responseService;
    @Autowired
    public SocialUserInterestInHashTagServiceValidator(ValidationService validationService, ResponseService responseService) {
        this.validationService = validationService;
        this.responseService = responseService;
    }
    // endregion

    @Around("execution(* com.social.seed.service.SocialUserInterestInHashTagService.addInterest(String, String)) && args(idUserRequest, idHashTag)")
    @Transactional
    public ResponseEntity<Object> aroundAddInterest(ProceedingJoinPoint joinPoint, String idUserRequest, String idHashTag) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }

        if (!validationService.hashTagExistsById(idHashTag)) {
            return responseService.hashTagNotFoundResponse(idHashTag);
        }

        if (validationService.existsInterest(idUserRequest, idHashTag)) {
            return responseService.conflictResponseWithMessage("The Interest already exists");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.SocialUserInterestInHashTagService.deleteInterest(String, String)) && args(idUserRequest, idHashTag)")
    @Transactional
    public ResponseEntity<Object> aroundDeleteInterest(ProceedingJoinPoint joinPoint, String idUserRequest, String idHashTag) throws Throwable {
        if (!validationService.userExistsById(idUserRequest)) {
            return responseService.userNotFoundResponse(idUserRequest);
        }

        if (!validationService.hashTagExistsById(idHashTag)) {
            return responseService.hashTagNotFoundResponse(idHashTag);
        }

        if (!validationService.existsInterest(idUserRequest, idHashTag)) {
            return responseService.conflictResponseWithMessage("The Interest does not exist");
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }
}
