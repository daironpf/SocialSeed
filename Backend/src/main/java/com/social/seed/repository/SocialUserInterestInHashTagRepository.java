package com.social.seed.repository;

import com.social.seed.model.HashTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;

public interface SocialUserInterestInHashTagRepository extends Neo4jRepository<HashTag, String> {

    @Query("""
            MATCH (u:SocialUser {id: $idUserRequest})
            MATCH (t:HashTag {id: $idHashTag})
            MERGE (u)-[r:INTERESTED_IN_HASHTAG {interestDate: $interestDate}]->(t)
            SET t.socialUserInterestIn = COALESCE(t.socialUserInterestIn, 0) + 1
            """)
    void addInterest(String idUserRequest, String idHashTag, LocalDateTime interestDate);

    @Query("""
            MATCH (u:SocialUser {id: $idUserRequest})
            MATCH (t:HashTag {id: $idHashTag})
            OPTIONAL MATCH (u)-[r:INTERESTED_IN_HASHTAG]->(t)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS existInterest
            """)
    Boolean existsInterest(String idUserRequest, String idHashTag);
}
