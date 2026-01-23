package com.socialseed.authservice.auth.domain.event;

import java.time.Instant;

public record PasswordResetRequestedEvent(
        String email,
        Instant timestamp
) {
    public PasswordResetRequestedEvent(String email) {
        this(email, Instant.now());
    }
}
