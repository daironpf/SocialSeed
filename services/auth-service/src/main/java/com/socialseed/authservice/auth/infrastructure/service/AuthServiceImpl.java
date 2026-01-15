package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.domain.service.TokenBlacklistService;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final UserRegisteredEventPublisher eventPublisher;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationSeconds;

    public AuthServiceImpl(AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JWTProvider jwtProvider,
            UserRegisteredEventPublisher eventPublisher,
            RefreshTokenRepository refreshTokenRepository,
            TokenBlacklistService tokenBlacklistService) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.eventPublisher = eventPublisher;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public AuthResponseDTO login(String email, String password) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, authUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtProvider.generateToken(authUser.getUsername());
        RefreshToken refreshToken = RefreshToken.create(authUser.getId(), refreshTokenDurationSeconds);
        refreshTokenRepository.save(refreshToken);

        Set<String> roles = authUser.getRoles();

        return new AuthResponseDTO(token, refreshToken.getToken(), roles);
    }

    @Transactional
    @Override
    public AuthResponseDTO register(AuthUser authUser, UUID id) {

        AuthUser newAuthUser = new AuthUser(
                id,
                authUser.getUsername(),
                authUser.getEmail(),
                passwordEncoder.encode(authUser.getPassword()));

        authUserRepository.save(newAuthUser);

        // Emit to Kafka Server
        UserRegisteredEvent event = new UserRegisteredEvent(
                id,
                authUser.getEmail(),
                authUser.getEmail(),
                System.currentTimeMillis());
        // eventPublisher.publish(event); // Kafka desactivado para pruebas locales sin
        // broker

        // Generate Tokens
        String token = jwtProvider.generateToken(newAuthUser.getUsername());
        RefreshToken refreshToken = RefreshToken.create(id, refreshTokenDurationSeconds);
        refreshTokenRepository.save(refreshToken);

        Set<String> roles = newAuthUser.getRoles();

        return new AuthResponseDTO(token, refreshToken.getToken(), roles);
    }

    @Override
    public AuthUser createUser(AuthUser authUser) {
        return null;
    }

    // region Gets
    @Override
    public Optional<AuthUser> getUserById(UUID id) {
        return authUserRepository.findById(id);
    }

    @Override
    public Optional<AuthUser> getUserByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public Optional<AuthUser> getUserByUserName(String username) {
        return authUserRepository.findByUserName(username);
    }
    // endregion

    // region Exists
    @Override
    public boolean existByUserId(UUID id) {
        return authUserRepository.existByUserId(id);
    }
    // endregion

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {

    }

    @Override
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        // 1. Invalidate Refresh Token
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
                token.revoke();
                refreshTokenRepository.save(token);
            });
        }

        // 2. Blacklist Access Token
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            String jti = jwtProvider.getJtiFromToken(token);
            Date expiry = jwtProvider.getExpirationDateFromToken(token);

            long ttlSeconds = (expiry.getTime() - System.currentTimeMillis()) / 1000;
            if (ttlSeconds > 0) {
                tokenBlacklistService.blacklistToken(jti, Duration.ofSeconds(ttlSeconds));
            }
        }
    }
}
