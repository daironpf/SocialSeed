package com.socialseed.authservice.auth.config.jwt;

import com.socialseed.authservice.auth.domain.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JWTProvider jwtProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final List<String> excludedPaths = List.of(
            "/about",
            // "/auth/**", // Allow filter to run for auth endpoints (e.g.,
            // /auth/{id}/roles)
            "/public/**",
            "/assets/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/actuator/**");

    public JwtAuthFilter(JWTProvider jwtProvider, TokenBlacklistService tokenBlacklistService) {
        this.jwtProvider = jwtProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return excludedPaths.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        log.info("JwtAuthFilter processing: {}", path);
        
        String header = request.getHeader("Authorization");
        log.info("Authorization header: {}", header != null ? "present (" + header.substring(0, Math.min(20, header.length())) + "...)" : "null");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtProvider.validateToken(token)) {
                String jti = jwtProvider.getJtiFromToken(token);
                log.info("Token valid, jti: {}", jti);
                
                if (tokenBlacklistService.isBlacklisted(jti)) {
                    log.info("Token is blacklisted");
                    filterChain.doFilter(request, response);
                    return;
                }

                String username = jwtProvider.getUsernameFromToken(token);
                log.info("Username from token: {}", username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        new User(username, "", Collections.emptyList()), null,
                        Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set for user: {}", username);
            } else {
                log.warn("Token validation failed");
            }
        }

        filterChain.doFilter(request, response);
    }
}