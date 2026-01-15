package com.socialseed.authservice.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {
    private final UUID id;
    private final String token;
    private final UUID userId;
    private final Instant expiryDate;
    private boolean revoked;

    public RefreshToken(UUID id, String token, UUID userId, Instant expiryDate, boolean revoked) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
    }

    public static RefreshToken create(UUID userId, long durationSeconds) {
        return new RefreshToken(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                userId,
                Instant.now().plusSeconds(durationSeconds),
                false);
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }
}
