package com.social.seed.repository;

import com.social.seed.dto.SocialUserCard;
import com.social.seed.model.SocialUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;

public interface FriendsRelationshipRepository extends Neo4jRepository<SocialUser, String> {
    //region REQUEST_FRIEND_TO
    @Query("""
            MATCH (b:SocialUser {identifier: $idUserRequest})
            MATCH (a:SocialUser {identifier: $idUserToBeFriend})
            OPTIONAL MATCH(b)-[r:REQUEST_FRIEND_TO]-(a)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS existRequest
            """)
    Boolean existsFriendRequest(String idUserRequest, String idUserToBeFriend);
    @Query("""
            MATCH (o:SocialUser {identifier: $idUserRequest})
            MATCH (d:SocialUser {identifier: $idUserToBeFriend})
            MERGE (o)-[r:REQUEST_FRIEND_TO {friendshipRequestDate: $now}]->(d)
            SET d.friendRequestCount = d.friendRequestCount + 1
            """)
    void createFriendRequest(String idUserRequest, String idUserToBeFriend, LocalDateTime now);
    @Query("""
            MATCH (o:SocialUser {identifier: $idUserRequest})
            MATCH (d:SocialUser {identifier: $idUserToCancelFriendRequest})
            MATCH (o)-[r:REQUEST_FRIEND_TO]-(d)
            DELETE r
            SET d.friendRequestCount = d.friendRequestCount - 1
            """)
    void cancelRequestFriendship(String idUserRequest, String idUserToCancelFriendRequest);
    @Query("""
            MATCH (o:SocialUser {identifier: $idUserRequest})
            MATCH (d:SocialUser {identifier: $idUserToAcceptedFriendRequest})
            MATCH (o)<-[r:REQUEST_FRIEND_TO]-(d)
            SET o.friendRequestCount = o.friendRequestCount - 1,
                o.friendCount = o.friendCount + 1,
                d.friendCount = d.friendCount + 1
            DELETE r
            MERGE (o)-[:FRIEND_OF {friendshipDate: $friendshipDate}]->(d)
            """)
    void acceptedRequestFriendship(String idUserRequest, String idUserToAcceptedFriendRequest, LocalDateTime friendshipDate);
    @Query("""
            MATCH (o:SocialUser {identifier: $idUserRequest})
            MATCH (d:SocialUser {identifier: $idUserToAcceptedFriendRequest})
            OPTIONAL MATCH(o)<-[r:REQUEST_FRIEND_TO]-(d)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS existRequestToAccepted
            """)
    boolean existsFriendRequestByUserToAccept(String idUserRequest, String idUserToAcceptedFriendRequest);
    //endregion

    //region FRIEND_OF
    @Query("""
            MATCH (b:SocialUser {identifier: $idUserRequest})
            MATCH (a:SocialUser {identifier: $idUserToBeFriend})
            OPTIONAL MATCH(b)-[r:FRIEND_OF]-(a)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS existFriend
            """)
    boolean existsFriendship(String idUserRequest, String idUserToBeFriend);

    @Query("""
                MATCH (o:SocialUser {identifier: $idUserRequest})
                MATCH (d:SocialUser {identifier: $idUserToDeleteFriendship})
                OPTIONAL MATCH (o)-[r:FRIEND_OF]-(d)
                FOREACH (_ IN CASE WHEN r IS NOT NULL THEN [1] ELSE [] END |
                    DELETE r
                    SET o.friendCount = o.friendCount - 1,
                        d.friendCount = d.friendCount - 1
                )
            """)
    void deleteFriendship(String idUserRequest, String idUserToDeleteFriendship);
    //endregion

    //region Get
    @Query(value = """
                MATCH (au:SocialUser {identifier: $idUserRequest})
                MATCH (uf:SocialUser)
                WHERE au <> uf AND NOT (uf)-[:FRIEND_OF]-(au)

                WITH uf, au
                SKIP $skip
                LIMIT $limit

                // find relationship with authenticated user
                OPTIONAL MATCH (au)<-[rfollow:FOLLOWED_BY]-(uf)
                OPTIONAL MATCH (au)-[rfollower:FOLLOWED_BY]->(uf)
                OPTIONAL MATCH (au)-[:FRIEND_OF]-(mf)-[:FRIEND_OF]-(uf)

                // Determine if the users are...
                WITH uf,
                    COUNT(rfollow) > 0 AS isFollow,
                    COUNT(rfollower) > 0 AS isFollower,
                    COUNT(mf) AS mutualFriends

                RETURN uf, isFollower, isFollow, mutualFriends, COALESCE(false, null) as isFriend
            """,
            countQuery = """
                MATCH (au:SocialUser {identifier: $idUserRequest})
                MATCH (uf:SocialUser)
                WHERE au <> uf AND NOT (uf)-[:FRIEND_OF]-(au)
                RETURN count(uf)
            """)
    Page<SocialUserCard> getFriendRecommendationsForUserById(String idUserRequest, Pageable pageable);

    @Query(value = """
                MATCH (o:SocialUser {identifier: $idUserToFind})-[fo:FRIEND_OF]-(friend)
                WITH friend
                ORDER BY fo.friendshipDate
                SKIP $skip
                LIMIT $limit

                // find the authenticated user
                MATCH (au:SocialUser {identifier: $userId})

                // find relationship with authenticated user
                OPTIONAL MATCH (au)-[rfriend:FRIEND_OF]-(friend)
                OPTIONAL MATCH (au)-[rrquestS:REQUEST_FRIEND_TO]->(friend)
                OPTIONAL MATCH (au)<-[rrquestR:REQUEST_FRIEND_TO]-(friend)
                OPTIONAL MATCH (au)-[rfollower:FOLLOWED_BY]->(friend)
                OPTIONAL MATCH (au)<-[rfollow:FOLLOWED_BY]-(friend)
                OPTIONAL MATCH (au)-[:FRIEND_OF]-(mf)-[:FRIEND_OF]-(friend)

                // Determine if the users are...
                WITH friend,
                    COUNT(rfriend) > 0 AS isFriend,
                    COUNT(rrquestS) > 0 AS isRequestFriendshipSending,
                    COUNT(rrquestR) > 0 AS isRequestFriendshipReceived,
                    COUNT(rfollower) > 0 AS isFollower,
                    COUNT(rfollow) > 0 AS isFollow,
                    COUNT(mf) AS mutualFriends

                RETURN friend, isFriend, isRequestFriendshipSending, isRequestFriendshipReceived, isFollower, isFollow, mutualFriends
            """,
            countQuery = """
                MATCH (:SocialUser {identifier: $idUserToFind})-[:FRIEND_OF]-(friend)
                RETURN count(friend)
            """)
    Page<SocialUserCard> getFriendsOfUserById(String userId, String idUserToFind, Pageable pageable);
    //endregion

}