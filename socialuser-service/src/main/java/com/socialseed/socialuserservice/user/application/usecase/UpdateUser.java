package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateUser {
    private final UserRepository userRepository;

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id, User updatedUser) {
//        userRepository.findById(id).ifPresent(existing -> {
//            updatedUser.setId(id);
//            userRepository.save(updatedUser);
//        });
    }
}
