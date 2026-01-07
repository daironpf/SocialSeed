package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.authservice.platform.validation.annotation.ValidPassword;
import com.socialseed.authservice.platform.validation.annotation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO (
    @ValidUsername
    String username,

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    String email,

    @ValidPassword
    String password
){}
