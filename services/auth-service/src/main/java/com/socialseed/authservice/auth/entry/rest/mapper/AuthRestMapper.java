package com.socialseed.authservice.auth.entry.rest.mapper;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.AuthUserResponseDTO;
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

    /* Domain → Response */
    public static AuthUserResponseDTO toResponse(AuthUser user) {
        return new AuthUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),

                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEmailVerified(),
                user.isTwoFactorEnabled(),

                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt()
        );
    }

    public static AuthResponseDTO toResponse(AuthResult result) {
        return new AuthResponseDTO(
                result.token(),
                result.refreshToken(),
                result.roles()
        );
    }
}
