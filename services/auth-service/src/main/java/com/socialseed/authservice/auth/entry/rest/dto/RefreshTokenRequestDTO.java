package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUUID;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
                @NotBlank(message = "{refresh.token.required}") 
                @ValidUUID String refreshToken
            ) {
}
