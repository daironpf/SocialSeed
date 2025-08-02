package com.our.socialseed.auth.config;

import com.our.socialseed.auth.infrastructure.security.JWTProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Bean
    public JWTProvider jwtProvider() {
        return new JWTProvider();
    }
}