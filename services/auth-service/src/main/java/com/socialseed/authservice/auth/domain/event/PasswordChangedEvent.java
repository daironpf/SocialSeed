package com.socialseed.authservice.auth.domain.event;

import java.util.UUID;

public record PasswordChangedEvent(
        UUID userId,
        String email,
        long occurredAt
) {
}
