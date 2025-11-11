package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUser {
    private final AuthService authService;

    public AuthenticateUser(AuthService authService) {
        this.authService = authService;
    }

    public AuthResponseDTO execute(String email, String password) {
        return authService.login(email, password);
    }
}