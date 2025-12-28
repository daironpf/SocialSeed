package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserByName {
    private final UserRepository userRepository;

    public GetUserByName(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(String userName){ return userRepository.findByUserName(userName); }
}
