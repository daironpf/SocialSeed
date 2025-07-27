package com.our.socialseed.user.config;

import com.our.socialseed.user.application.service.UserServiceImpl;
import com.our.socialseed.user.domain.port.in.UserService;
import com.our.socialseed.user.domain.port.out.UserRepository;
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
