package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.platform.error.BusinessException;
import com.socialseed.authservice.platform.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetUserById {
    private final AuthService authService;

    public GetUserById(AuthService authService) {
        this.authService = authService;
    }

    public Optional<AuthUser> execute(UUID userId) {
        return Optional.ofNullable(authService.getUserById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        userId
                )));
    }
}