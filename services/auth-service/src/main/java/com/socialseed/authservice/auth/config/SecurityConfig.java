package com.socialseed.authservice.auth.config;

import com.socialseed.authservice.auth.config.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .anonymous()
                .and()
                .addFilterBefore(jwtAuthFilter, FilterSecurityInterceptor.class)
                .authorizeRequests()
                        .accessDecisionManager(accessDecisionManager())
                        .requestMatchers("/auth/register", "/auth/login", "/auth/forgot-password",
                                "/auth/reset-password", "/auth/verify", "/auth/verify-email",
                                "/auth/resend-verification", "/auth/token/refresh",
                                "/auth/getUserById/**", "/auth/getUserByEmail/**",
                                "/auth/getUserByUserName/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        WebExpressionVoter expressionVoter = new WebExpressionVoter();
        voters.add(expressionVoter);
        voters.add(new RoleVoter());
        voters.add(new AuthenticatedVoter());
        return new AffirmativeBased(voters);
    }
}
