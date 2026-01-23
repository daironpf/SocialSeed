package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequestDTO(
        @NotBlank(message = "Token is required")
        String token
) {
}
