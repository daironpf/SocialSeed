package com.socialseed.authservice.auth.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

    private static final UUID TEST_USER_ID = UUID.randomUUID();

    @Test
    void create_shouldGenerateValidToken() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);

        assertThat(token.getId()).isNotNull();
        assertThat(token.getToken()).isNotNull();
        assertThat(token.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(token.getExpiryDate()).isAfter(Instant.now());
        assertThat(token.isRevoked()).isFalse();
        assertThat(token.isRotated()).isFalse();
    }

    @Test
    void create_shouldGenerateUniqueIds() {
        RefreshToken token1 = RefreshToken.create(TEST_USER_ID, 86400);
        RefreshToken token2 = RefreshToken.create(TEST_USER_ID, 86400);

        assertThat(token1.getId()).isNotEqualTo(token2.getId());
        assertThat(token1.getToken()).isNotEqualTo(token2.getToken());
    }

    @Test
    void create_shouldSetCorrectExpiryDuration() {
        long durationSeconds = 3600;
        Instant beforeCreation = Instant.now();

        RefreshToken token = RefreshToken.create(TEST_USER_ID, durationSeconds);

        Instant afterCreation = Instant.now();
        assertThat(token.getExpiryDate()).isAfter(beforeCreation.plusSeconds(durationSeconds).minusMillis(100));
        assertThat(token.getExpiryDate()).isBefore(afterCreation.plusSeconds(durationSeconds).plusMillis(100));
    }

    @Test
    void revoke_shouldMarkTokenAsRevoked() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);

        token.revoke();

        assertThat(token.isRevoked()).isTrue();
    }

    @Test
    void rotate_shouldMarkTokenAsRotated() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);

        token.rotate();

        assertThat(token.isRotated()).isTrue();
    }

    @Test
    void isExpired_shouldReturnFalseForFutureExpiry() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);

        assertThat(token.isExpired()).isFalse();
    }

    @Test
    void isExpired_shouldReturnTrueForPastExpiry() {
        Instant pastExpiry = Instant.now().minusSeconds(3600);
        RefreshToken token = new RefreshToken(
                UUID.randomUUID(),
                "test-token",
                TEST_USER_ID,
                pastExpiry,
                false,
                false
        );

        assertThat(token.isExpired()).isTrue();
    }

    @Test
    void isValid_shouldReturnTrueForActiveToken() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);

        assertThat(token.isValid()).isTrue();
    }

    @Test
    void isValid_shouldReturnFalseForRevokedToken() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);
        token.revoke();

        assertThat(token.isValid()).isFalse();
    }

    @Test
    void isValid_shouldReturnFalseForRotatedToken() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);
        token.rotate();

        assertThat(token.isValid()).isFalse();
    }

    @Test
    void isValid_shouldReturnFalseForExpiredToken() {
        Instant pastExpiry = Instant.now().minusSeconds(3600);
        RefreshToken token = new RefreshToken(
                UUID.randomUUID(),
                "test-token",
                TEST_USER_ID,
                pastExpiry,
                false,
                false
        );

        assertThat(token.isValid()).isFalse();
    }

    @Test
    void isValid_shouldReturnFalseForRevokedAndRotatedToken() {
        RefreshToken token = RefreshToken.create(TEST_USER_ID, 86400);
        token.revoke();
        token.rotate();

        assertThat(token.isValid()).isFalse();
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        UUID id = UUID.randomUUID();
        String tokenStr = "test-token-string";
        Instant expiry = Instant.now().plusSeconds(7200);

        RefreshToken token = new RefreshToken(id, tokenStr, TEST_USER_ID, expiry, false, false);

        assertThat(token.getId()).isEqualTo(id);
        assertThat(token.getToken()).isEqualTo(tokenStr);
        assertThat(token.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(token.getExpiryDate()).isEqualTo(expiry);
        assertThat(token.isRevoked()).isFalse();
        assertThat(token.isRotated()).isFalse();
    }

    @Test
    void constructor_shouldAcceptRevokedAndRotatedFlags() {
        RefreshToken token = new RefreshToken(
                UUID.randomUUID(),
                "test-token",
                TEST_USER_ID,
                Instant.now().plusSeconds(3600),
                true,
                true
        );

        assertThat(token.isRevoked()).isTrue();
        assertThat(token.isRotated()).isTrue();
    }
}
