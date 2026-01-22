package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidPassword;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "Current password is required")
        @ValidPassword String currentPassword,

        @NotBlank(message = "New password is required")
        @ValidPassword String newPassword
) {
}