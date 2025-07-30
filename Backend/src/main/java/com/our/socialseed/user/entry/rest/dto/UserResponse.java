package com.our.socialseed.user.entry.rest.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String fullName
) {
}
