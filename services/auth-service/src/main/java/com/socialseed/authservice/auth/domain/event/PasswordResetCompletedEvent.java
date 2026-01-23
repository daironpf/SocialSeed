package com.socialseed.authservice.auth.domain.event;

import java.time.Instant;
import java.util.UUID;

public record PasswordResetCompletedEvent(
        UUID userId,
        Instant timestamp
) {
    public PasswordResetCompletedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
