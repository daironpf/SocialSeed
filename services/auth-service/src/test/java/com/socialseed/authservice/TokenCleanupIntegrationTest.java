package com.socialseed.authservice;

import com.socialseed.authservice.auth.application.usecase.CleanupExpiredTokens;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.infrastructure.service.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TokenCleanupIntegrationTest {

    @Autowired
    private CleanupExpiredTokens cleanupExpiredTokens;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    private UUID userId;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
        // Need a user to associate tokens
        userId = UUID.randomUUID();
        AuthUser authUser = new AuthUser(userId, "tokenuser", "token@example.com", "Password123!");
        authService.register(authUser, userId);
    }

    @Test
    void shouldCleanupExpiredTokens() {
        // 1. Create Expired Token
        RefreshToken expiredToken = new RefreshToken(
                UUID.randomUUID(),
                "expired-token-123",
                userId,
                Instant.now().minus(1, ChronoUnit.DAYS), // Expired yesterday
                false,
                false);
        refreshTokenRepository.save(expiredToken);

        // 2. Create Valid Token
        RefreshToken validToken = new RefreshToken(
                UUID.randomUUID(),
                "valid-token-456",
                userId,
                Instant.now().plus(1, ChronoUnit.DAYS), // Expires tomorrow
                false,
                false);
        refreshTokenRepository.save(validToken);

        // 3. Verify Initial State
        assertTrue(refreshTokenRepository.findByToken("expired-token-123").isPresent());
        assertTrue(refreshTokenRepository.findByToken("valid-token-456").isPresent());

        // 4. Run Cleanup
        cleanupExpiredTokens.execute();

        // 5. Verify Final State
        assertFalse(refreshTokenRepository.findByToken("expired-token-123").isPresent(),
                "Expired token should be deleted");
        assertTrue(refreshTokenRepository.findByToken("valid-token-456").isPresent(), "Valid token should remain");
    }
}
