package com.our.socialseed.auth.domain.service;

import com.our.socialseed.auth.domain.model.AuthUser;
import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
import com.our.socialseed.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {
    AuthResponseDTO login(String email, String password);
    AuthResponseDTO register(RegisterRequestDTO dto);

    AuthUser createUser(AuthUser authUser);
    Optional<AuthUser> getUserById(UUID id);
    Optional<AuthUser> getUserByEmail(String email);
    void changePassword(UUID userId, String currentPassword, String newPassword);

}
