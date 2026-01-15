package com.socialseed.authservice.auth.infrastructure.persistence.pgsql.mapper;

import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.infrastructure.persistence.pgsql.entity.RefreshTokenPgsqlEntity;

public class RefreshTokenMapper {
    public static RefreshToken toDomain(RefreshTokenPgsqlEntity entity) {
        if (entity == null)
            return null;
        return new RefreshToken(
                entity.getId(),
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiryDate(),
                entity.isRevoked());
    }

    public static RefreshTokenPgsqlEntity toEntity(RefreshToken domain) {
        if (domain == null)
            return null;
        return new RefreshTokenPgsqlEntity(
                domain.getId(),
                domain.getToken(),
                domain.getUserId(),
                domain.getExpiryDate(),
                domain.isRevoked());
    }
}
