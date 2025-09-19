package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
//        user.setRoles(Set.of("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Roles: "+user.getRoles());

        return userRepository.save(user);
    }
}
