package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
                @JsonProperty("refreshToken")
                @NotBlank(message = "{refresh.token.required}") 
                @ValidUUID String refreshToken
            ) {
}
