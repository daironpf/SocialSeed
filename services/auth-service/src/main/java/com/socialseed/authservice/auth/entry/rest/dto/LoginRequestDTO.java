package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
                @NotBlank(message = "{user.email.required}") 
                @Email(message = "{user.email.invalid}") 
                String email,
                
                @NotBlank(message = "{user.password.required}") 
                @ValidPassword String password
        ) {
}
