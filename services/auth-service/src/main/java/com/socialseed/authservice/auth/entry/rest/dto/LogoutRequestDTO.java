package com.socialseed.authservice.auth.entry.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @JsonProperty("refreshToken")
        @NotBlank(message = "{auth.logout.refreshToken.required}") 
        String refreshToken
) {
}
