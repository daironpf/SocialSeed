package com.socialseed.socialuserservice.user.domain.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
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

    private LocalDateTime dateBorn;
    private String language;
    private String profileImage;
    private String bio;

    private Boolean onVacation;
    private Boolean isActive;
    private Boolean isDeleted;


}