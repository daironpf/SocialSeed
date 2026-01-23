package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class RefreshToken {
    private final AuthService authService;

    public RefreshToken(AuthService authService) {
        this.authService = authService;
    }

    public AuthResult execute(String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
