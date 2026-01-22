package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialseed.validation.annotation.ValidPassword;

/**
 * DTO para el cambio de contraseña.
 */
public record PasswordChangeRequest(
        @JsonProperty("currentPassword")
        @NotBlank(message = "{user.currentpassword.required}") 
        @ValidPassword String currentPassword,

        @JsonProperty("newPassword")
        @NotBlank(message = "{user.newpassword.required}") 
        @ValidPassword String newPassword,

        @JsonProperty("id")
        @NotBlank(message = "{user.id.required}") 
        String id
    ) {
}