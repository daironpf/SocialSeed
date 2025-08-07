package com.our.socialseed.auth.infrastructure.config;

import com.our.socialseed.shared.security.jwt.JWTProvider;
import com.our.socialseed.shared.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers(
                                "/auth/**",                 // login, register
                                "/public/**",               // recursos estáticos, imágenes públicas
                                "/assets/**",               // estáticos (si aplica)
                                "/v3/api-docs/**",          // documentación OpenAPI
                                "/swagger-ui/**",           // Swagger UI assets
                                "/swagger-ui.html"          // antigua URL
                        ).permitAll()
                        .anyRequest().authenticated()  // Todo lo demás requiere autenticación
                )
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
