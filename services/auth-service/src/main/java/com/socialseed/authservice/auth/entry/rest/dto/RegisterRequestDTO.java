package com.socialseed.authservice.auth.entry.rest.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO (
//    @ValidUsername
    String username,

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    String email,

//    @ValidPassword
    String password
){}
