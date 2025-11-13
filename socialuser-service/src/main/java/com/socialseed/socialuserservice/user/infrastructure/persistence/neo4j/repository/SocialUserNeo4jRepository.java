package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity.UserNeo4jEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;
import java.util.UUID;

public interface SocialUserNeo4jRepository extends Neo4jRepository<UserNeo4jEntity, UUID> {
    // region findBy
    Optional<UserNeo4jEntity> findByEmail(String email);
    //endregion

    //region existBy
    @Query("""
            MATCH (u:SocialUser)
            WHERE u.email = $email
            RETURN COUNT(u) > 0
            """)
    Boolean existByEmail(String email);

    @Query("""
            MATCH (u:SocialUser)
            WHERE u.username = $userName
            RETURN COUNT(u) > 0
            """)
    Boolean existByUserName(String userName);

    @Query("""
            MATCH (u:SocialUser)
            WHERE u.id = $id
            RETURN COUNT(u) > 0
            """)
    Boolean existByUserId(UUID id);
    //endregion
}