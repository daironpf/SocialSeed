package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "{user.email.required}") @Email(message = "{user.email.invalid}") String email,

        @NotBlank(message = "{user.password.required}") @Size(min = 6, max = 60, message = "{user.password.size}") String password) {
}
