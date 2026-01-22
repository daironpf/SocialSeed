package com.socialseed.authservice.auth.entry.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialseed.validation.annotation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
                @JsonProperty("email")
                @NotBlank(message = "{user.email.required}") 
                @Email(message = "{user.email.invalid}") 
                String email,
                
                @JsonProperty("password")
                @NotBlank(message = "{user.password.required}") 
                @ValidPassword String password
        ) {
}
