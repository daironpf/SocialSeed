package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateUser {
    private final UserRepository userRepository;

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id, User updatedUser) {
        userRepository.findById(id).ifPresent(existing -> {
            updatedUser.setId(id);
            updatedUser.setPassword(existing.getPassword()); // keep original password
            userRepository.save(updatedUser);
        });
    }
}
