package com.our.socialseed.user.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @NotBlank(message = "{user.username.required}")
        @Size(max = 30, message = "{user.username.size}")
        String username,

        @NotBlank(message = "{user.email.required}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "{user.password.required}")
        @Size(min = 6, max = 60, message = "{user.password.size}")
        String password,

        @NotBlank(message = "{user.fullname.required}")
        @Size(max = 100, message = "{user.fullname.size}")
        String fullName
) {
}