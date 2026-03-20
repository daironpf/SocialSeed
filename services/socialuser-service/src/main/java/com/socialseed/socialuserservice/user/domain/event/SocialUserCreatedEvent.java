package com.socialseed.socialuserservice.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SocialUserCreatedEvent(
        UUID userId,
        String email,
        String username,
        Instant occurredAt
) implements DomainEvent {
    public SocialUserCreatedEvent(UUID userId, String email, String username) {
        this(userId, email, username, Instant.now());
    }
}
