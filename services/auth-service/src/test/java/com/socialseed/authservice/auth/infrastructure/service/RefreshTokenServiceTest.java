package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.domain.repository.PasswordChangedEventPublisher;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.socialseed.authservice.auth.domain.service.TokenBlacklistService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private UserRegisteredEventPublisher eventPublisher;
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    @Mock
    private PasswordChangedEventPublisher passwordChangedEventPublisher;
    @Mock
    private com.socialseed.authservice.auth.domain.service.LoginAttemptService loginAttemptService;

    private AuthServiceImpl authService;

    private final UUID userId = UUID.randomUUID();
    private final String oldTokenStr = "old-refresh-token";
    private final long duration = 3600;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                authUserRepository,
                passwordEncoder,
                jwtProvider,
                eventPublisher,
                refreshTokenRepository,
                tokenBlacklistService,
                passwordChangedEventPublisher,
                loginAttemptService);
        ReflectionTestUtils.setField(authService, "refreshTokenDurationSeconds", duration);
    }

    @Test
    void shouldRotateTokenSuccessfully() {
        // Arrange
        RefreshToken oldToken = new RefreshToken(UUID.randomUUID(), oldTokenStr, userId,
                Instant.now().plusSeconds(duration), false, false);
        AuthUser user = new AuthUser(userId, "testuser", "test@example.com", "password");

        when(refreshTokenRepository.findByToken(oldTokenStr)).thenReturn(Optional.of(oldToken));
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtProvider.generateToken(any())).thenReturn("new-access-token");

        // Act
        AuthResponseDTO response = authService.refreshToken(oldTokenStr);

        // Assert
        assertNotNull(response);
        assertEquals("new-access-token", response.token());
        assertNotEquals(oldTokenStr, response.refreshToken());
        assertTrue(oldToken.isRotated());

        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class)); // 1 for rotating old, 1 for new
    }

    @Test
    void shouldDetectReuseAndRevokeAllSessions() {
        // Arrange
        RefreshToken reusedToken = new RefreshToken(UUID.randomUUID(), oldTokenStr, userId,
                Instant.now().plusSeconds(duration), false, true); // Already rotated

        when(refreshTokenRepository.findByToken(oldTokenStr)).thenReturn(Optional.of(reusedToken));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.refreshToken(oldTokenStr));
        assertEquals(ErrorCode.AUTH_REUSE_DETECTION, exception.getErrorCode());

        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    @Test
    void shouldFailIfTokenExpired() {
        // Arrange
        RefreshToken expiredToken = new RefreshToken(UUID.randomUUID(), oldTokenStr, userId,
                Instant.now().minusSeconds(10), false, false);

        when(refreshTokenRepository.findByToken(oldTokenStr)).thenReturn(Optional.of(expiredToken));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.refreshToken(oldTokenStr));
        assertEquals(ErrorCode.REFRESH_TOKEN_INVALID_EXPIRED, exception.getErrorCode());
    }

    @Test
    void shouldFailIfTokenRevoked() {
        // Arrange
        RefreshToken revokedToken = new RefreshToken(UUID.randomUUID(), oldTokenStr, userId,
                Instant.now().plusSeconds(duration), true, false);

        when(refreshTokenRepository.findByToken(oldTokenStr)).thenReturn(Optional.of(revokedToken));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.refreshToken(oldTokenStr));
        assertEquals(ErrorCode.REFRESH_TOKEN_INVALID_EXPIRED, exception.getErrorCode());
    }
}
