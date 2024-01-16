package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;

public interface FriendsRepository extends Neo4jRepository<SocialUser, String> {
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
}