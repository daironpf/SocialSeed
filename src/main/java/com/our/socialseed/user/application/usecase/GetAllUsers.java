package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;

import java.util.List;

public class GetAllUsers {
    private final UserRepository userRepository;

    public GetAllUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return userRepository.findAll();
    }
}
