package com.socialseed.socialuserservice.user.entry.rest.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String fullName
) {
}
