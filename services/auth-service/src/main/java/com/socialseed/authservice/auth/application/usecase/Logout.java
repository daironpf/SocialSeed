package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class Logout {
    private final AuthService authService;

    public Logout(AuthService authService) {
        this.authService = authService;
    }

    public void execute(String accessToken, String refreshToken) {
        authService.logout(accessToken, refreshToken);
    }
}
