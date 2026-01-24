package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequestDTO(
    @NotBlank(message = "{user.email.required}") @Email(message = "{user.email.invalid}") String newEmail) {
}
