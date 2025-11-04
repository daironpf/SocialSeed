package com.socialseed.socialuserservice.user.infrastructure.persistence;

import com.socialseed.socialuserservice.user.infrastructure.persistence.entity.UserNeo4jEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends Neo4jRepository<UserNeo4jEntity, UUID> {
    Optional<UserNeo4jEntity> findByEmail(String email);
    // Puedes agregar métodos como findByEmail, etc. si quieres

    //region existBy
    @Query("""
            OPTIONAL MATCH (u:SocialUser {email: $email})
            RETURN CASE WHEN u IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existByEmail(String email);

    @Query("""
            OPTIONAL MATCH (u:SocialUser {username: $userName})
            RETURN CASE WHEN u IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existByUserName(String userName);
    //endregion
}