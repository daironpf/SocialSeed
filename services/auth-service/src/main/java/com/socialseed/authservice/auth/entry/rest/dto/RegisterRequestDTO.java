package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialseed.validation.annotation.ValidPassword;

public record RegisterRequestDTO(
                @JsonProperty("username")
                @ValidUsername String username,

                @JsonProperty("email")
                @NotBlank(message = "{user.email.required}") 
                @Email(message = "{user.email.invalid}") 
                String email,

                @JsonProperty("password")
                @NotBlank(message = "{user.password.required}") 
                @ValidPassword String password
            ) {
}
