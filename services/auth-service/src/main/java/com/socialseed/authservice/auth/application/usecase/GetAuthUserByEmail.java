package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAuthUserByEmail {
    private final AuthService authService;

    public GetAuthUserByEmail(AuthService authService) {
        this.authService = authService;
    }

    public Optional<AuthUser> execute(@Valid String email) {
        return Optional.ofNullable(authService.getUserByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_EMAIL_NOT_FOUND,
                        email
                )));
    }
}
