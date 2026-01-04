package com.socialseed.socialuserservice.user.entry.rest.mapper;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UserCreateRequestDTO;
import com.socialseed.socialuserservice.user.entry.rest.dto.response.UserResponseDTO;

public class UserRestMapper {
    private UserRestMapper() {}

    public static User toDomain(UserCreateRequestDTO req) {

        return User.create(
                req.username(),
                req.email()
        );
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getBirthDate(),
                user.getLanguage(),
                user.getProfileImage(),
                user.getBio(),
                user.getStatus()
        );
    }
}