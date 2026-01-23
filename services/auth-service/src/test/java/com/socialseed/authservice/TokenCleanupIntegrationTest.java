package com.socialseed.authservice;

import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.AuthUserPgsqlEntity;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.repository.AuthUserPgsqlRepository;
import com.socialseed.authservice.auth.infrastructure.scheduler.TokenCleanupScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class TokenCleanupIntegrationTest {

    @Autowired
    private AuthUserPgsqlRepository authUserRepository;

    @Autowired
    private TokenCleanupScheduler tokenCleanupScheduler;

    @BeforeEach
    void setUp() {
        authUserRepository.deleteAll();
    }

    @Test
    void shouldClearExpiredTokens() {
        // Arrange
        Instant expired = Instant.now().minus(1, ChronoUnit.HOURS);
        Instant valid = Instant.now().plus(1, ChronoUnit.HOURS);

        // User with expired reset token
        AuthUserPgsqlEntity userExpiredReset = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .email("user1@example.com")
                .password("pass")
                .resetPasswordToken("token1")
                .resetPasswordTokenExpiry(expired)
                .build();

        // User with valid reset token
        AuthUserPgsqlEntity userValidReset = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("user2")
                .email("user2@example.com")
                .password("pass")
                .resetPasswordToken("token2")
                .resetPasswordTokenExpiry(valid)
                .build();

        // User with expired verification token
        AuthUserPgsqlEntity userExpiredVerify = AuthUserPgsqlEntity.builder()
                .id(UUID.randomUUID())
                .username("user3")
                .email("user3@example.com")
                .password("pass")
                .verificationToken("token3")
                .verificationTokenExpiry(expired)
                .emailVerified(false)
                .build();

        authUserRepository.save(userExpiredReset);
        authUserRepository.save(userValidReset);
        authUserRepository.save(userExpiredVerify);

        // Act
        tokenCleanupScheduler.cleanupExpiredTokens();

        // Assert
        AuthUserPgsqlEntity result1 = authUserRepository.findById(userExpiredReset.getId()).orElseThrow();
        assertNull(result1.getResetPasswordToken());
        assertNull(result1.getResetPasswordTokenExpiry());

        AuthUserPgsqlEntity result2 = authUserRepository.findById(userValidReset.getId()).orElseThrow();
        assertNotNull(result2.getResetPasswordToken());
        assertNotNull(result2.getResetPasswordTokenExpiry());

        AuthUserPgsqlEntity result3 = authUserRepository.findById(userExpiredVerify.getId()).orElseThrow();
        assertNull(result3.getVerificationToken());
        assertNull(result3.getVerificationTokenExpiry());
    }
}
