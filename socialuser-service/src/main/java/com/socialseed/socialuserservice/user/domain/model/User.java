package com.socialseed.socialuserservice.user.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    private String fullName;
}