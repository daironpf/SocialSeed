package com.our.socialseed.user.infrastructure.persistence.mapper;

import com.our.socialseed.user.infrastructure.persistence.entity.UserNeo4jEntity;
import com.our.socialseed.user.domain.model.User;

public class UserNeo4jMapper {
    private UserNeo4jMapper() {
        // Clase de utilidad, no instanciable
    }

    public static UserNeo4jEntity toNode(User user) {
        return new UserNeo4jEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFullName()
        );
    }

    public static User toDomain(UserNeo4jEntity node) {
        return new User(
                node.getId(),
                node.getUsername(),
                node.getEmail(),
                node.getPassword(),
                node.getFullName()
        );
    }
}
