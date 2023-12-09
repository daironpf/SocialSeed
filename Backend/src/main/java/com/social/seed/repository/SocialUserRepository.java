package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SocialUserRepository extends Neo4jRepository<SocialUser, String> {

    //region existBy
    @Query("""
            OPTIONAL MATCH (u:SocialUser {email: $email})
            RETURN CASE WHEN u IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existByEmail(String email);

    @Query("""
            OPTIONAL MATCH (u:SocialUser {userName: $userName})
            RETURN CASE WHEN u IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existByUserName(String userName);
    //endregion

    //region findBy
    @Query("""
            OPTIONAL MATCH (u:SocialUser {email: $email})
            RETURN u
            """)
    Optional<SocialUser> findByEmail(String email);
    //endregion

    //region crud
    @Override
    @Query("""
            OPTIONAL MATCH (u:SocialUser {id: $id})
            RETURN u
            """)
    Optional<SocialUser> findById(String id);

    @Query("""
            MATCH (u:SocialUser {id: $id})
            SET u.fullName = $fullName,
                u.dateBorn = $dateBorn,
                u.language = $language
            """)
    void update(
            String id,
            String fullName,
            LocalDateTime dateBorn,
            String language
    );
    //endregion

    //region Update Special Props
    @Query("MATCH (u:SocialUser {id: $id}) SET u.userName = $newUserName")
    void updateSocialUserName(String id, String newUserName);

    @Query("MATCH (u:SocialUser {id: $id}) SET u.email = $newEmail")
    void updateSocialUserEmail(String id, String newEmail);
    //endregion
}