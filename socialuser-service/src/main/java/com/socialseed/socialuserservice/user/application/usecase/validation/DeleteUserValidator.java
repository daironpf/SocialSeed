package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.platform.error.UserWithIdNotFoundException;
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

    @Around("execution(* com.socialseed.socialuserservice.user.application.usecase.DeleteUser.execute(..)) && args(id)")
    public Object aroundCreateNewSocialUser(ProceedingJoinPoint joinPoint, UUID id) throws Throwable {
        if (!validationService.userExistByUserId(id)) {
            throw new UserWithIdNotFoundException("A SocialUser with this Id not Found: "+id);
        }

        return joinPoint.proceed();
    }
}
