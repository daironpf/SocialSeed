package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.platform.error.BusinessException;
import com.socialseed.socialuserservice.platform.error.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class DeleteUserValidator {
    private static final Logger log = LoggerFactory.getLogger(CreateUserValidator.class);
    private final ValidationService validationService;

    public DeleteUserValidator(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Around("execution(* com.socialseed.socialuserservice.user.application.usecase.DeleteUser.execute(..)) && args(userId)")
    public Object aroundCreateNewSocialUser(ProceedingJoinPoint joinPoint, UUID userId) throws Throwable {
        if (!validationService.userExistByUserId(userId)) {
            throw new BusinessException(ErrorCode.INVALID_ID, userId);
        }



        return joinPoint.proceed();
    }
}
