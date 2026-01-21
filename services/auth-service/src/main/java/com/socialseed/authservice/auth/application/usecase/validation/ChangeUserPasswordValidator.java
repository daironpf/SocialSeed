package com.socialseed.authservice.auth.application.usecase.validation;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class ChangeUserPasswordValidator {

    private final ValidationService validationService;

    public ChangeUserPasswordValidator(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Around(value = "execution(* com.socialseed.authservice.auth.application.usecase.ChangeUserPassword.execute(..)) " +
            "&& args(userId, currentPassword)", argNames = "joinPoint,userId,currentPassword")
    public Object aroundChangeUserPassword(ProceedingJoinPoint joinPoint, UUID userId, String currentPassword) throws Throwable{
        if (!validationService.userExistByUserId(userId)) {
            throw new BusinessException(ErrorCode.INVALID_ID, userId);
        }
        if (validationService.isCurrentPasswordValid(userId, currentPassword)){
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
        return joinPoint.proceed();
    }
}
