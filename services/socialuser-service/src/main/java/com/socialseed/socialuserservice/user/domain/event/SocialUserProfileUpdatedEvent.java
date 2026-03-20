package com.socialseed.socialuserservice.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SocialUserProfileUpdatedEvent(
        UUID userId,
        Instant occurredAt
) implements DomainEvent {
    public SocialUserProfileUpdatedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
