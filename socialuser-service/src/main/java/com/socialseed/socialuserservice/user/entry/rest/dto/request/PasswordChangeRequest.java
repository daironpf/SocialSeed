package com.socialseed.socialuserservice.user.entry.rest.dto.request;

import jakarta.validation.constraints.NotBlank;


/**
 * DTO para el cambio de contraseña.
 */
public record PasswordChangeRequest(
        @NotBlank(message = "{user.currentpassword.required}")
        String currentPassword,
        @NotBlank(message = "{user.newpassword.required}")
        String newPassword,
        @NotBlank(message = "{user.id.required}")
        String id
) {
}

