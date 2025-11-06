package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

@Component
public class ValidationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ValidationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean userExistByEmail(String email) {
        return userRepository.existByEmail(email);
    }
    public boolean userExistByUserName(String username) {
        return userRepository.existByUsername(username);
    }

    public boolean userExistByUserId(UUID id) {
        return userRepository.existByUserId(id);
    }

    public boolean isCurrentPasswordValid(UUID userId, String currentPassword) {
        User user = userRepository.findById(userId).get();
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }
}
