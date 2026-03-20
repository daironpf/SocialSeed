package com.socialseed.authservice.auth.application.usecase.validation;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RegisterUserValidator {

    private static final Logger log = LoggerFactory.getLogger(RegisterUserValidator.class);

    private final ValidationService validationService;

    public RegisterUserValidator(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Around(value = "execution(* com.socialseed.authservice.auth.application.usecase.RegisterUser.execute(..)) " +
            "&& args(authUser)", argNames = "joinPoint,authUser")
    public Object aroundRegisterUser(ProceedingJoinPoint joinPoint, AuthUser authUser) throws Throwable {
        log.info("RegisterUserValidator - Around advice invoked for email: {}", authUser.getEmail());
        if (validationService.userExistByEmail(authUser.getEmail())) {
            log.warn("RegisterUserValidator - User email already exists: {}", authUser.getEmail());
            throw new BusinessException(ErrorCode.USER_EMAIL_EXISTS, authUser.getEmail());
        }
        if (validationService.userExistByUserName(authUser.getUsername())) {
            log.warn("RegisterUserValidator - Username already exists: {}", authUser.getUsername());
            throw new BusinessException(ErrorCode.USERNAME_EXISTS, authUser.getUsername());
        }
        log.info("RegisterUserValidator - Validation passed, proceeding with execution");
        return joinPoint.proceed();
    }
}
