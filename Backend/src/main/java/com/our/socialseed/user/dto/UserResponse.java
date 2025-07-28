package com.our.socialseed.user.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String fullName
) {
}
