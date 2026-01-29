package com.socialseed.authservice.auth.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record RemoveRoleRequestDTO(
    @NotBlank(message = "User ID is required")
    String userId,
    
    @NotBlank(message = "Role is required")
    String role
) {}