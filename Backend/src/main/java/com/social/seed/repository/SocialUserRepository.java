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

    @Query("""
            OPTIONAL MATCH (u:SocialUser {userName: $userName})
            RETURN u
            """)
    Optional<SocialUser> findByUserName(String userName);
    //endregion

    //region CRUD
    @Override
    @Query("""
            OPTIONAL MATCH (u:SocialUser {identifier: $id})
            OPTIONAL MATCH (u)-[rt]->(t:HashTag)
            RETURN u, collect(rt), collect(t)
            """)
    Optional<SocialUser> findById(String id);

    @Query("""
            MATCH (u:SocialUser {identifier: $id})
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
    @Query("MATCH (u:SocialUser {identifier: $id}) SET u.userName = $newUserName")
    void updateSocialUserName(String id, String newUserName);

    @Query("MATCH (u:SocialUser {identifier: $id}) SET u.email = $newEmail")
    void updateSocialUserEmail(String id, String newEmail);
    //endregion

    //region FOLLOW
    @Query("""
            MATCH (b:SocialUser {identifier: $userBId})
            MATCH (a:SocialUser {identifier: $userAId})
            OPTIONAL MATCH(b)<-[r:FOLLOWED_BY]-(a)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS following
            """)
    Boolean isUserBFollowerOfUserA(String userBId, String userAId);

    @Query("""
            MATCH (b:SocialUser {identifier: $userBId})
            MATCH (a:SocialUser {identifier: $userAId})
            MERGE(b)<-[r:FOLLOWED_BY {followDate:$followDate}]-(a)
            WITH a, b
            SET a.followersCount = a.followersCount + 1,
                b.followingCount = b.followingCount + 1
            """)
    void createUserBFollowUserA(
            String userBId,
            String userAId,
            LocalDateTime followDate);

    @Query("""
            MATCH (b:SocialUser {identifier: $userBId})
            MATCH (a:SocialUser {identifier: $userAId})
            MATCH (b)<-[r:FOLLOWED_BY]-(a)
            DELETE r
            WITH a, b
            SET a.followersCount = a.followersCount - 1,
                b.followingCount = b.followingCount - 1
            """)
    void unFollowTheUserA(String userBId, String userAId);
    //endregion

    //region Vacation Mode
    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            RETURN u.onVacation
            """)
    Boolean isVacationModeActivated(String idUserRequest);
    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            SET u.onVacation = true
            """)
    void activateVacationMode(String idUserRequest);
    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            SET u.onVacation = false
            """)
    void deactivateVacationMode(String idUserRequest);
    //endregion

    //region Delete
    @Override
    @Query("""
            MATCH (u:SocialUser {identifier: $id})
            OPTIONAL MATCH (u)<-[:POSTED_BY]-(p)
            FOREACH (_ IN CASE WHEN u IS NOT NULL THEN [1] ELSE [] END |                    
                    DETACH DELETE u
                )
            FOREACH (_ IN CASE WHEN p IS NOT NULL THEN [1] ELSE [] END |
                    DETACH DELETE p
                )
            """)
    void deleteById(String id);
    //endregion

    //region Activated Mode
    @Query("""
            MATCH (u:SocialUser {identifier: $id})
            RETURN u.isActive
            """)
    boolean isSocialUserActivated(String id);

    @Query("""
            MATCH (u:SocialUser {identifier: $id})
            SET u.isActive= true
            """)
    void activateSocialUser(String id);

    @Query("""
            MATCH (u:SocialUser {identifier: $id})
            SET u.isActive= false
            """)
    void deactivateSocialUser(String id);
    //endregion
}