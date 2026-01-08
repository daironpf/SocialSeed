package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.mapper;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity.UserNeo4jEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserNeo4jMapperImpl implements UserNeo4jMapper {

    @Override
    public UserNeo4jEntity toEntity(User domain) {
        Objects.requireNonNull(domain, "domain user must not be null");

        return UserNeo4jEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .fullName(domain.getFullName())
                .birthDate(domain.getBirthDate())
                .language(domain.getLanguage().name())
                .profileImage(domain.getProfileImage())
                .bio(domain.getBio())
                .status(domain.getStatus().name())
                .build();
    }

    @Override
    public User toDomain(UserNeo4jEntity entity) {
        Objects.requireNonNull(entity, "neo4j entity must not be null");

        return User.rehydrate(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFullName(),
                entity.getBirthDate(),
                parseLanguage(entity.getLanguage()),
                entity.getProfileImage(),
                entity.getBio(),
                parseStatus(entity.getStatus())
        );
    }

    private UserLanguage parseLanguage(String value) {
        try {
            return UserLanguage.valueOf(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid language stored in DB: " + value);
        }
    }

    private UserStatus parseStatus(String value) {
        try {
            return UserStatus.valueOf(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid status stored in DB: " + value);
        }
    }
}
