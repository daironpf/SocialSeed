package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(
    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    String email
) {}
