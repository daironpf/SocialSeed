package com.socialseed.authservice.auth.entry.rest.mapper;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;

public class AuthRestMapper {
    private AuthRestMapper() {}

    public static AuthUser toDomain(RegisterRequestDTO req) {
        return new AuthUser(
                null,
                req.username(),
                req.email(),
                req.password()
        );
    }

//    public static UserResponseDTO toResponse(AuthUser user) {
//        return new UserResponseDTO(
//                user.getId(),
//                user.getUsername(),
//                user.getEmail(),
//                user.getFullName()
//        );
//    }
}
