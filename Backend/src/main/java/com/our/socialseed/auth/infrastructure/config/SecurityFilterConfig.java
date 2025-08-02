package com.our.socialseed.auth.infrastructure.config;

import com.our.socialseed.auth.infrastructure.security.JWTProvider;
import com.our.socialseed.auth.infrastructure.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTProvider jwtProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()    // Login, register, etc.
                        .requestMatchers("/public/**").permitAll()  // Fotos públicas, recursos estáticos
                        .requestMatchers("/auth/**", "/public/**", "/assets/**").permitAll()
                        .anyRequest().authenticated()               // Todo lo demás requiere autenticación
                )
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}