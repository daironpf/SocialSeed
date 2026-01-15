package com.socialseed.authservice.auth.config;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.config.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Buena práctica añadirla
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTProvider jwtProvider;
    private final com.socialseed.authservice.auth.domain.service.TokenBlacklistService tokenBlacklistService;

    public SecurityConfig(JWTProvider jwtProvider,
                          com.socialseed.authservice.auth.domain.service.TokenBlacklistService tokenBlacklistService) {
        this.jwtProvider = jwtProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Ya no necesitas 'new AntPathRequestMatcher(...)', usa solo el String
                        .requestMatchers("/auth/**", "/public/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(jwtProvider, tokenBlacklistService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}