package com.socialseed.authservice.auth.domain.event;

import java.time.Instant;
import java.util.UUID;

public record RoleRemovedEvent(
    UUID userId,
    String email,
    String username,
    String role,
    UUID removedBy,
    Instant removedAt
) {}