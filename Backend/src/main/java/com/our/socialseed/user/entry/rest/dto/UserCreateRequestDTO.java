package com.our.socialseed.user.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @Size(max = 30, message = "El username no debe superar los 30 caracteres")
        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email con formato inválido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 60)
        String password,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 100)
        String fullName
) {
}