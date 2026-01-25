package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.constraints.NotBlank;

public record ChangeUsernameRequestDTO(
    @NotBlank(message = "{username.empty}") @ValidUsername String newUsername) {
}
