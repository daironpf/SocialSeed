package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.socialseed.validation.annotation.ValidPassword;

public record RegisterRequestDTO(
                @ValidUsername String username,

                @NotBlank(message = "{user.email.required}") @Email(message = "{user.email.invalid}") String email,

                @NotBlank(message = "{user.password.required}") @ValidPassword String password) {
}
