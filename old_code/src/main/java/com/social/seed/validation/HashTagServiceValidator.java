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

import com.social.seed.model.HashTag;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.annotation.Transactional;

/**
 * Validator class for the {@link com.social.seed.service.HashTagService}, focusing on validating and ensuring consistency
 * in the operations related to managing { Hashtags within the SocialSeed application }.
 * <p>
 * Author: Dairon Pérez Frías
 * Since: 2024-01-16
 */
@Aspect
@Component
public class HashTagServiceValidator {
    //region dependencies
    private final ValidationService validationService;
    private final ResponseService responseService;

    @Autowired
    public HashTagServiceValidator(ValidationService validationService, ResponseService responseService) {
        this.validationService = validationService;
        this.responseService = responseService;
    }
    //endregion

    @Around("execution(* com.social.seed.service.HashTagService.getHashTagById(String)) && args(id)")
    public ResponseEntity<Object> aroundGetHashTagById(ProceedingJoinPoint joinPoint, String id) throws Throwable {
        if (!validationService.hashTagExistsById(id)) {
            return responseService.hashTagNotFoundResponse(id);
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.HashTagService.createNewHashTag(com.social.seed.model.HashTag)) && args(newHashTag)")
    @Transactional
    public ResponseEntity<Object> aroundCreateNewHashTag(ProceedingJoinPoint joinPoint, HashTag newHashTag) throws Throwable {
        if (validationService.hashTagExistsByName(newHashTag.getName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The HashTag with the name [ %s ] already exists", newHashTag.getName())
            );
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.HashTagService.updateHashTag(com.social.seed.model.HashTag)) && args(updatedHashTag)")
    public ResponseEntity<Object> aroundUpdateHashTag(ProceedingJoinPoint joinPoint, HashTag updatedHashTag) throws Throwable {
        if (!validationService.hashTagExistsById(updatedHashTag.getId())) {
            return responseService.hashTagNotFoundResponse(updatedHashTag.getId());
        }

        if (validationService.hashTagExistsByName(updatedHashTag.getName())) {
            return responseService.conflictResponseWithMessage(
                    String.format("The HashTag with the name [ %s ] already exists", updatedHashTag.getName())
            );
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }

    @Around("execution(* com.social.seed.service.HashTagService.deleteHashTag(String)) && args(id)")
    @Transactional
    public ResponseEntity<Object> aroundDeleteHashTag(ProceedingJoinPoint joinPoint, String id) throws Throwable {
        if (!validationService.hashTagExistsById(id)) {
            return responseService.hashTagNotFoundResponse(id);
        }

        // Continue
        return (ResponseEntity<Object>) joinPoint.proceed();
    }
}