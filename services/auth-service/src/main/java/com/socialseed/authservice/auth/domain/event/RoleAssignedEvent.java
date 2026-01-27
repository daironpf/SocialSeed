package com.socialseed.authservice.auth.domain.event;

import java.time.Instant;
import java.util.UUID;

public record RoleAssignedEvent(
    UUID userId,
    String email,
    String username,
    String role,
    UUID assignedBy,
    Instant assignedAt
) {}