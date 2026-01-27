package com.socialseed.authservice.auth.domain.service;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;

import java.util.Optional;
import java.util.UUID;

public interface AuthService {
    AuthResult login(String email, String password, String ip);

    AuthResult register(AuthUser auth, UUID id);

    Optional<AuthUser> getUserById(UUID id);

    Optional<AuthUser> getUserByEmail(String email);

    Optional<AuthUser> getUserByUserName(String username);

    AuthUser createUser(AuthUser authUser);

    void changePassword(UUID userId, String currentPassword, String newPassword);

    boolean existByUserId(UUID id);

    void logout(String accessToken, String refreshToken);

    AuthResult refreshToken(String refreshToken);

    void verifyEmail(String token);

    void resendVerificationEmail(String email);

    void saveUser(AuthUser user);
}