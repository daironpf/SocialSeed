package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidationService {
    private final UserRepository userRepository;

    public ValidationService(UserRepository userRepository) {
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
}
