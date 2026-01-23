package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(
    @NotBlank(message = "{token.required}")
    String token,

    @ValidPassword
    String newPassword
) {}
