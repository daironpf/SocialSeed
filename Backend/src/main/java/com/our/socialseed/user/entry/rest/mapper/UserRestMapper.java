package com.our.socialseed.user.entry.rest.mapper;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.entry.rest.dto.UserCreateRequestDTO;
import com.our.socialseed.user.entry.rest.dto.UserResponseDTO;
import com.our.socialseed.user.entry.rest.dto.UserUpdateRequestDTO;

public class UserRestMapper {
    private UserRestMapper() {}

    public static User UpdatetoDomain(UserUpdateRequestDTO req) {
        return new User(
                null,                           // id se asignará en el servicio
                req.username(),
                req.email(),
                null,
                req.fullName()
        );
    }

    public static User toDomain(UserCreateRequestDTO req) {
        return new User(
                null,                           // id se asignará en el servicio
                req.username(),
                req.email(),
                req.password(),                 // sin hash por ahora
                req.fullName()
        );
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
