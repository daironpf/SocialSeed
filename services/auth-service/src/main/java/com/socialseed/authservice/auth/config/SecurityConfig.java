package com.socialseed.authservice.auth.config;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.config.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
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
                                .csrf(csrf -> csrf.disable()) // desactiva CSRF si usas API REST
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtAuthFilter(jwtProvider, tokenBlacklistService),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}