package com.socialseed.authservice.auth.application.usecase.validation;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.platform.error.BusinessException;
import com.socialseed.authservice.platform.error.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RegisterUserValidator {

    private final ValidationService validationService;

    public RegisterUserValidator(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Around(value = "execution(* com.socialseed.authservice.auth.application.usecase.RegisterUser.execute(..)) " +
            "&& args(authUser)", argNames = "joinPoint,authUser")
    public Object aroundRegisterUser(ProceedingJoinPoint joinPoint, AuthUser authUser) throws Throwable {
        if (validationService.userExistByEmail(authUser.getEmail())) {
            throw new BusinessException(ErrorCode.USER_EMAIL_EXISTS, authUser.getEmail());
        }
        if (validationService.userExistByUserName(authUser.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS, authUser.getUsername());
        }
        return joinPoint.proceed();
    }
}
