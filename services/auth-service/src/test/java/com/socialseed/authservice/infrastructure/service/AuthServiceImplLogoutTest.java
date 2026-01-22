package com.socialseed.authservice.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.socialseed.authservice.auth.domain.service.TokenBlacklistService;
import com.socialseed.authservice.auth.infrastructure.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplLogoutTest {

    @Mock
    private AuthUserRepository authUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private UserRegisteredEventPublisher eventPublisher;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                authUserRepository,
                passwordEncoder,
                jwtProvider,
                eventPublisher,
                refreshTokenRepository,
                tokenBlacklistService);
        ReflectionTestUtils.setField(authService, "refreshTokenDurationSeconds", 3600L);
    }

    @Test
    void shouldInvalidateRefreshTokenWhenLogout() {
        String refreshTokenStr = "valid-refresh-token";
        UUID userId = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID(), refreshTokenStr, userId,
                new Date().toInstant().plusSeconds(3600), false, false);

        when(refreshTokenRepository.findByToken(refreshTokenStr)).thenReturn(Optional.of(refreshToken));

        authService.logout(null, refreshTokenStr);

        assert refreshToken.isRevoked();
        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    void shouldBlacklistAccessTokenWhenLogout() {
        String accessToken = "Bearer valid-access-token";
        String jti = "jwt-id-123";
        Date expiry = new Date(System.currentTimeMillis() + 60000); // 1 minute from now

        when(jwtProvider.getJtiFromToken("valid-access-token")).thenReturn(jti);
        when(jwtProvider.getExpirationDateFromToken("valid-access-token")).thenReturn(expiry);

        authService.logout(accessToken, null);

        verify(tokenBlacklistService).blacklistToken(eq(jti), any(Duration.class));
    }
}
