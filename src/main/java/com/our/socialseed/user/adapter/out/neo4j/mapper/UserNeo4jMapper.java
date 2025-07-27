package com.our.socialseed.user.adapter.out.neo4j.mapper;

import com.our.socialseed.user.adapter.out.neo4j.UserNode;
import com.our.socialseed.user.domain.model.User;

public class UserNeo4jMapper {
    private UserNeo4jMapper() {
        // Clase de utilidad, no instanciable
    }

    public static UserNode toNode(User user) {
        return new UserNode(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFullName()
        );
    }

    public static User toDomain(UserNode node) {
        return new User(
                node.getId(),
                node.getUsername(),
                node.getEmail(),
                node.getPassword(),
                node.getFullName()
        );
    }
}
