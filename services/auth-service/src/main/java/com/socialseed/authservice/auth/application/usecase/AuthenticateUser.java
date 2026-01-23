package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUser {
    private final AuthService authService;

    public AuthenticateUser(AuthService authService) {
        this.authService = authService;
    }

    public AuthResult execute(String email, String password, String ip) {
        return authService.login(email, password, ip);
    }
}