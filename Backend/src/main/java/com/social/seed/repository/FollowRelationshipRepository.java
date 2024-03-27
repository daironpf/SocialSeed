/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import com.social.seed.service.SocialUserService;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository focusing on managing { Follow Relationship }.
 * <p>
 * @author Dairon Pérez Frías
 * @since 2024-03-27
 */
public interface FollowRelationshipRepository extends Neo4jRepository<SocialUser, String> {

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

    //region Recommendations
    @Query("""
            MATCH (o:SocialUser {identifier: $idUserRequest})
            MATCH (u:SocialUser)
            WHERE u <> o AND NOT (u)-[:FRIEND_OF]->(o)
            RETURN u
            LIMIT 3
            """)
    List<SocialUser> getLiteFriendRecommendationsForUserById(String idUserRequest);
    //endregion
}
