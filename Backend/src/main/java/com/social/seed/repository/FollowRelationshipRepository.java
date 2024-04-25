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

import com.social.seed.dto.SocialUserCard;
import com.social.seed.model.SocialUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //region Get
    @Query(value = """
                MATCH (o:SocialUser {identifier: $idUserRequest})
                MATCH (u:SocialUser)
                WHERE u <> o AND NOT (u)-[:FOLLOWED_BY]->(o)
                //WITH u, rand() AS random
                RETURN u
                SKIP $skip
                LIMIT $limit
                //ORDER BY random
            """,
            countQuery = """
                MATCH (o:SocialUser {identifier: $idUserRequest})
                MATCH (u:SocialUser)
                WHERE u <> o AND NOT (u)-[:FOLLOWED_BY]->(o)
                RETURN count(u)
            """)
    Page<SocialUser> getFollowRecommendationsForUserById(String idUserRequest, Pageable pageable);

    @Query(value = """
                MATCH (o:SocialUser {identifier: $idUserRequest})<-[fb:FOLLOWED_BY]-(uf:SocialUser)
                WITH uf
                ORDER BY fb.followDate
                SKIP $skip
                LIMIT $limit

                // find the authenticated user
                MATCH (au:SocialUser {identifier: $userId})

                // find relationship with authenticated user
                OPTIONAL MATCH (au)-[rfriend:FRIEND_OF]-(uf)
                OPTIONAL MATCH (au)-[rfollower:FOLLOWED_BY]->(uf)
                OPTIONAL MATCH (au)-[:FRIEND_OF]-(mf)-[:FRIEND_OF]-(uf)

                // Determine if the users are...
                WITH uf,
                    COUNT(rfriend) > 0 AS isFriend,
                    COUNT(rfollower) > 0 AS isFollower,
                    COUNT(mf) AS mutualFriends

                RETURN uf, isFriend, isFollower, mutualFriends
            """,
            countQuery = """
                MATCH (o:SocialUser {identifier: $idUserRequest})<-[fb:FOLLOWED_BY]-(u:SocialUser)
                RETURN count(o)
            """)
    Page<SocialUserCard> getFollowingForUserById(String userId, String idUserRequest, Pageable pageable);

    @Query(value = """
                // find followers
                MATCH (uo:SocialUser {identifier: $idUserRequest})-[fb:FOLLOWED_BY]->(uf:SocialUser)
                WITH uf
                ORDER BY fb.followDate
                SKIP $skip
                LIMIT $limit

                // find the authenticated user
                MATCH (au:SocialUser {identifier: $userId})

                // find relationship with authenticated user
                OPTIONAL MATCH (au)-[rfriend:FRIEND_OF]-(uf)
                OPTIONAL MATCH (au)-[rfollower:FOLLOWED_BY]->(uf)
                OPTIONAL MATCH (au)-[:FRIEND_OF]-(mf)-[:FRIEND_OF]-(uf)

                // Determine if the users are...
                WITH uf,
                    COUNT(rfriend) > 0 AS isFriend,
                    COUNT(rfollower) > 0 AS isFollower,
                    COUNT(mf) AS mutualFriends

                RETURN uf, isFriend, isFollower, mutualFriends
            """,
            countQuery = """
                MATCH (o:SocialUser {identifier: $idUserRequest})-[fb:FOLLOWED_BY]->(u:SocialUser)
                RETURN count(o)
            """)
    Page<SocialUserCard> getFollowersBySocialUserId(String userId, String idUserRequest, Pageable pageable);
    //endregion
}