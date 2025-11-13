package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeUserPassword {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public ChangeUserPassword(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO execute(UUID userId, String currentPassword, String newPassword) {
        AuthUser authUser = authService.getUserById(userId).get();
        authUser.setPassword(passwordEncoder.encode(newPassword));
        authService.changePassword(userId, currentPassword, newPassword);
        return null;
    }
}
