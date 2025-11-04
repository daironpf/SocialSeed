package com.socialseed.socialuserservice.user.application.usecase.validation;

import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidationService {
    private final UserRepository userRepository;

    public ValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userExistByEmail(String email) {
        return this.userRepository.existByEmail(email);
    }
    public boolean userExistByUserName(String username) {
        return this.userRepository.existByUsername(username);
    }
}
