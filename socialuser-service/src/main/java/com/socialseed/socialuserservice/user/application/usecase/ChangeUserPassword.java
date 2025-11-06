package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChangeUserPassword {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangeUserPassword(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
