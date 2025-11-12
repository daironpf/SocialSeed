package com.socialseed.authservice.auth.domain.service;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {
    AuthResponseDTO login(String email, String password);
    AuthResponseDTO register(RegisterRequestDTO dto, UUID id);

    AuthUser createUser(AuthUser authUser);
    Optional<AuthUser> getUserById(UUID id);
    Optional<AuthUser> getUserByEmail(String email);
    void changePassword(UUID userId, String currentPassword, String newPassword);

}