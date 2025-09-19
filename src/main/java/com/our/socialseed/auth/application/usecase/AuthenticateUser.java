package com.our.socialseed.auth.application.usecase;

import com.our.socialseed.auth.domain.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUser {
    private final AuthService authService;

    public AuthenticateUser(AuthService authService) {
        this.authService = authService;
    }

    public String execute(String email, String password) {
        return authService.login(email, password);
    }
}
