package com.our.socialseed.user.config;

import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public UserUseCases userUseCases(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        return new UserUseCases(userRepository, passwordEncoder);
    }
}
