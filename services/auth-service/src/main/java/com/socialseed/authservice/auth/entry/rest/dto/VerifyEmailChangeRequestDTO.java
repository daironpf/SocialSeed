package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailChangeRequestDTO(
    @NotBlank String token) {
}
