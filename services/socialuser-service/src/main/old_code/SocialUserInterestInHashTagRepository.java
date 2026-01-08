package com.social.seed.repository;

import com.social.seed.model.HashTag;
import com.social.seed.model.SocialUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SocialUserInterestInHashTagRepository extends Neo4jRepository<HashTag, String> {

    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (t:HashTag {identifier: $idHashTag})
            OPTIONAL MATCH (u)-[r:INTERESTED_IN_HASHTAG]->(t)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS existInterest
            """)
    boolean existsInterest(String idUserRequest, String idHashTag);

    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (t:HashTag {identifier: $idHashTag})
            MERGE (u)-[r:INTERESTED_IN_HASHTAG {interestDate: $interestDate}]->(t)
            SET t.socialUserInterestIn = COALESCE(t.socialUserInterestIn, 0) + 1
            """)
    void addInterest(String idUserRequest, String idHashTag, LocalDateTime interestDate);

    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (t:HashTag {identifier: $idHashTag})
            MATCH (u)-[r:INTERESTED_IN_HASHTAG]->(t)
            DELETE r
            SET t.socialUserInterestIn = t.socialUserInterestIn - 1
            """)
    void deleteInterest(String idUserRequest, String idHashTag);

    @Override
    @Query("""
            MATCH (:SocialUser)-[r:INTERESTED_IN_HASHTAG]->(:HashTag)
            DELETE r
            """)
    void deleteAll();

    @Query("""
            OPTIONAL MATCH (u:SocialUser {identifier: $idUserRequest})
            OPTIONAL MATCH (u)-[INTERESTED_IN_HASHTAG]->(t:HashTag)
            RETURN t
            """)
    Optional<List<HashTag>> getInterestBySocialUserId(String idUserRequest);
}
