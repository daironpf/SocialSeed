package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeUserPassword {
    private final AuthService authService;

    public ChangeUserPassword(AuthService authService) {
        this.authService = authService;
    }

    public void execute(UUID userId, String currentPassword, String newPassword) {
        authService.changePassword(userId, currentPassword, newPassword);
    }
}
