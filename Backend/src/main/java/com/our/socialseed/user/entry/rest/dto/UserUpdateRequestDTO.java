package com.our.socialseed.user.entry.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @NotBlank(message = "{user.username.required}")
        @Size(max = 30)
        String username,

        @NotBlank(message = "{user.email.required}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "{user.fullname.required}")
        @Size(max = 100)
        String fullName
) {
}
