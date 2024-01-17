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

@Aspect
@Component
public class ValidationHashtagService {
    @Autowired
    private ValidationService validationService;

    @Autowired
    private ResponseService responseService;

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

