package com.socialseed.socialuserservice.user.entry.rest.dto.response;

import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String fullName,
        LocalDate birthDate,
        UserLanguage language,
        String profileImage,
        String bio,
        UserStatus status
) {
}