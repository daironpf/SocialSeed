package com.our.socialseed.user.config;

import com.our.socialseed.user.application.service.UserServiceImpl;
import com.our.socialseed.user.domain.port.in.UserService;
import com.our.socialseed.user.domain.port.out.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig {
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}
