package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.repository.UserRepository;

import java.util.UUID;

public class DeleteUser {
    private final UserRepository userRepository;

    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id) {
        userRepository.deleteById(id);
    }
}
