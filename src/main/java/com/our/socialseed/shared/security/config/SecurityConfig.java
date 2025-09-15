package com.our.socialseed.shared.security.config;

import com.our.socialseed.shared.security.jwt.JwtAuthFilter;
import com.our.socialseed.shared.security.jwt.JWTProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JWTProvider jwtProvider;

    public SecurityConfig(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desactiva CSRF si usas API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/about",    // informacion sobre la red social
                                "/auth/**",              // login, register
                                "/public/**",            // recursos estáticos, imágenes públicas
                                "/assets/**",            // estáticos (si aplica)
                                "/swagger-ui/**",        // Swagger UI assets
                                "/v3/api-docs/**",       // documentación OpenAPI
                                "/swagger-ui.html"       // antigua URL
                        ).permitAll() // ✅ acceso público
                        .anyRequest().authenticated() // 🔒 resto protegido
                )
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
