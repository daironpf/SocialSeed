package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.mapper;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity.UserNeo4jEntity;
import com.socialseed.socialuserservice.user.domain.model.User;

public class UserNeo4jMapper {
    private UserNeo4jMapper() {
        // Clase de utilidad, no instanciable
    }

    public static UserNeo4jEntity toNode(User user) {
        return UserNeo4jEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    public static User toDomain(UserNeo4jEntity node) {
        return User.builder()
                .id(node.getId())
                .username(node.getUsername())
                .email(node.getEmail())
                .fullName(node.getFullName())
                .build();
    }
}
