package com.socialseed.authservice.auth.entry.rest.dto;

import com.socialseed.validation.annotation.ValidUUID;
import com.socialseed.validation.annotation.ValidRole;
import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequestDTO(
                @NotBlank(message = "user.id.required") @ValidUUID(message = "error.invalid.uuid") String userId,

                @NotBlank(message = "auth.role.required") @ValidRole(message = "auth.error.invalid_role") String role) {
}