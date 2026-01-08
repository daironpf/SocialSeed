package com.socialseed.authservice.auth.domain.event;

public record UserRegisteredEvent(
        java.util.UUID userId,
        String email,
        String username,
        long occurredAt
) {}
