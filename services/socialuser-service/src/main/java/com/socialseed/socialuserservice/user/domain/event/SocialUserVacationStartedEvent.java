package com.socialseed.socialuserservice.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SocialUserVacationStartedEvent(
        UUID userId,
        Instant startedAt
) implements DomainEvent {
    public SocialUserVacationStartedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
