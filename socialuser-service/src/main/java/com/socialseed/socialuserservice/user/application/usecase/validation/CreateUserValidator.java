package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.platform.error.BusinessException;
import com.socialseed.socialuserservice.platform.error.ErrorCode;
import com.socialseed.socialuserservice.user.domain.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CreateUserValidator {
    private static final Logger log = LoggerFactory.getLogger(CreateUserValidator.class);
    private final ValidationService validationService;

    public CreateUserValidator(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Around("execution(* com.socialseed.socialuserservice.user.application.usecase.CreateUser.execute(..)) && args(user)")
    public Object aroundCreateNewSocialUser(ProceedingJoinPoint joinPoint, User user) throws Throwable {
        if (validationService.userExistByEmail(user.getEmail())) {
            throw new BusinessException(ErrorCode.USER_EMAIL_EXISTS, user.getEmail());
        }
        if (validationService.userExistByUserName(user.getUsername())){
            throw new BusinessException(ErrorCode.USERNAME_EXISTS, user.getUsername());
        }

        return joinPoint.proceed();
    }
}