package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.mapper;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity.UserNeo4jEntity;

public interface UserNeo4jMapper {

    UserNeo4jEntity toEntity(User domain);

    User toDomain(UserNeo4jEntity entity);
}
