package com.socialseed.authservice.auth.application.usecase.validation;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;

    public ValidationService(PasswordEncoder passwordEncoder, AuthUserRepository authUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authUserRepository = authUserRepository;
    }

    public boolean userExistByEmail(String email) {
        return authUserRepository.existByEmail(email);
    }
    public boolean userExistByUserName(String username) {
        return authUserRepository.existByUsername(username);
    }

    public boolean userExistByUserId(UUID id) {
        return authUserRepository.existByUserId(id);
    }

    public boolean isCurrentPasswordValid(UUID userId, String currentPassword) {
        AuthUser user = authUserRepository.findById(userId).get();
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
