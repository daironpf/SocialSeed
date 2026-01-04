package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity.UserNeo4jEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface SocialUserNeo4jRepository extends Neo4jRepository<UserNeo4jEntity, UUID> {
    @Query("""
        MATCH (u:SocialUser {id: $id})
        SET
            u.full_name = $fullName,
            u.bio = $bio,
            u.profile_image = $profileImage,
            u.birth_date = $birthDate,
            u.language = $language
        """)
    void updateProfile(
            UUID id,
            String fullName,
            String bio,
            String profileImage,
            LocalDate birthDate,
            String language
    );

    // region findBy
    @Query("""
            MATCH (u:SocialUser {email: $email})
            RETURN u
            LIMIT 1
        """)
    Optional<UserNeo4jEntity> findByEmail(String email);

    @Query("""
            MATCH (u:SocialUser {username: $username})
            RETURN u
            LIMIT 1
        """)
    Optional<UserNeo4jEntity> findByUsername(String username);

    @Override
    @Query("""
            MATCH (u:SocialUser {id: $id})
            RETURN u
            LIMIT 1
        """)
    Optional<UserNeo4jEntity> findById(UUID id);
    //endregion

    //region existBy
    @Query("""
            MATCH (u:SocialUser {email: $email})
            RETURN true
            LIMIT 1
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