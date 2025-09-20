package com.our.socialseed.auth.application.usecase;

import com.our.socialseed.auth.domain.service.AuthService;
import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
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
