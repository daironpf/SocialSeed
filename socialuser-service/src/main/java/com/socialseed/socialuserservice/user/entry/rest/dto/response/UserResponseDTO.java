package com.socialseed.socialuserservice.user.entry.rest.dto.response;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String fullName
) {
}
