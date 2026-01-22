package com.socialseed.authservice.auth.domain.service;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {
    AuthResponseDTO login(String email, String password);

    AuthResponseDTO register(AuthUser auth, UUID id);

    Optional<AuthUser> getUserById(UUID id);

    Optional<AuthUser> getUserByEmail(String email);

    Optional<AuthUser> getUserByUserName(String username);

    AuthUser createUser(AuthUser authUser);

    void changePassword(UUID userId, String currentPassword, String newPassword);

    boolean existByUserId(UUID id);

    void logout(String accessToken, String refreshToken);

    AuthResponseDTO refreshToken(String refreshToken);
}