package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeUserPassword {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public ChangeUserPassword(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO execute(UUID userId, String currentPassword, String newPassword) {
        if(authService.existByUserId(userId)){
            AuthUser authUser = authService.getUserById(userId).get();
            log.info("User {} existe", authUser.getUsername());
            authUser.setPassword(passwordEncoder.encode(newPassword));
            authService.changePassword(userId, currentPassword, newPassword);
        }else {
            log.info("User not found");
        }

        return null;
    }
}
