package com.our.socialseed.user.dto;

import com.our.socialseed.user.domain.model.User;

public class UserDtoMapper {
    private UserDtoMapper() {}

    public static User toDomain(UserRequest req) {
        return new User(
                null,                           // id se asignará en el servicio
                req.username(),
                req.email(),
                req.password(),                 // sin hash por ahora
                req.fullName()
        );
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
