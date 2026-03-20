package com.socialseed.socialuserservice.user.domain.event;

import java.time.Instant;
import java.util.UUID;

public record SocialUserVacationEndedEvent(
        UUID userId,
        Instant endedAt
) implements DomainEvent {
    public SocialUserVacationEndedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
