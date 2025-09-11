package com.our.socialseed.auth.config;

import com.our.socialseed.auth.application.usecase.AuthUseCases;
import com.our.socialseed.auth.domain.service.AuthService;
import com.our.socialseed.shared.security.jwt.JWTProvider;
import com.our.socialseed.auth.infrastructure.service.AuthServiceImpl;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AuthUseCasesConfig {
    @Bean
    public AuthService authService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   JWTProvider jwtProvider) {
        return new AuthServiceImpl(userRepository, passwordEncoder, jwtProvider);
    }

    @Bean
    public AuthUseCases authUseCases(AuthService authService) {
        return new AuthUseCases(authService);
    }
}
