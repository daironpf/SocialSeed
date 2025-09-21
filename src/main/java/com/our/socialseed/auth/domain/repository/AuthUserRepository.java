package com.our.socialseed.auth.domain.repository;

import com.our.socialseed.auth.domain.model.AuthUser;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
    AuthUser save(AuthUser authUser);

    Optional<AuthUser> findById(UUID id);
    Optional<AuthUser> findByEmail(String email);
}
