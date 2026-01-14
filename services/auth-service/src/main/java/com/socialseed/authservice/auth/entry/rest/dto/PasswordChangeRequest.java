package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;
import com.socialseed.validation.annotation.ValidPassword;

/**
 * DTO para el cambio de contraseña.
 */
public record PasswordChangeRequest(
                @NotBlank(message = "{user.currentpassword.required}") String currentPassword,
                @NotBlank(message = "{user.newpassword.required}") @ValidPassword String newPassword,
                @NotBlank(message = "{user.id.required}") String id) {
}