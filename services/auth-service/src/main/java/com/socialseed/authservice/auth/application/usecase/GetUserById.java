package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
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
        return authService.getUserById(userId);
    }
}