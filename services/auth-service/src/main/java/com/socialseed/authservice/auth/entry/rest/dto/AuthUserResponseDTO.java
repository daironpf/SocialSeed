package com.socialseed.authservice.auth.entry.rest.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AuthUserResponseDTO(
                UUID id,
                String username,
                String email,
                Set<String> roles,

                boolean enabled,
                boolean accountNonExpired,
                boolean accountNonLocked,
                boolean credentialsNonExpired,
                boolean emailVerified,
                boolean twoFactorEnabled,

                Instant createdAt,
                Instant updatedAt,
                Instant lastLoginAt
        ) {
}
