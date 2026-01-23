package com.socialseed.authservice.auth.domain.repository;

import com.socialseed.authservice.auth.domain.model.AuthUser;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
    AuthUser save(AuthUser authUser);

    Optional<AuthUser> findById(UUID id);
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByUserName(String username);

    boolean existByUserId(UUID id);
    boolean existByUsername(String username);
    boolean existByEmail(String email);

    Optional<AuthUser> findByResetPasswordToken(String token);
    Optional<AuthUser> findByVerificationToken(String token);

}