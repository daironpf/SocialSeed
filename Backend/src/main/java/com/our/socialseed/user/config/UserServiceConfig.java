package com.our.socialseed.user.config;

import com.our.socialseed.user.application.usecase.UserServiceImpl;
import com.our.socialseed.user.domain.service.UserService;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserServiceConfig {
    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }
}
