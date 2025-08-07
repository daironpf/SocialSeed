package com.our.socialseed.user.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @Size(max = 30, message = "{user.username.size}")
        @NotBlank(message = "{user.username.required}")
        String username,

        @NotBlank(message = "{user.email.required}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 60)
        String password,

        @NotBlank(message = "{user.fullname.required}")
        @Size(max = 100)
        String fullName
) {
}