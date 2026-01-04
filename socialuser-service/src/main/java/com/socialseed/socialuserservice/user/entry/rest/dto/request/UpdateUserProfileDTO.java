package com.socialseed.socialuserservice.user.entry.rest.dto.request;

import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileDTO(
        @NotNull(message = "{user.id.required}")
        UUID userId,

        @NotBlank(message = "{user.fullname.required}")
        @Size(max = 100, message = "{user.fullname.size}")
        String fullName,

        @Size(max = 500, message = "{user.bio.size}")
        String bio,

        @Pattern(
                regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
                message = "{user.profileImage.invalid}"
        )
        String profileImage,

        @Past(message = "{user.birthDate.past}")
        LocalDate birthDate,

        @NotNull(message = "{user.language.required}")
        UserLanguage language
) {
}
