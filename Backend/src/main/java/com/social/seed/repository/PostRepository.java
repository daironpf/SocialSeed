package com.social.seed.repository;

import com.social.seed.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;

public interface PostRepository extends Neo4jRepository<Post, String> {
    //region gets
    @Override
    @Query(value ="""
                MATCH (p:Post)
                WITH p ORDER BY p.updateDate DESC
                SKIP $skip LIMIT $limit
            
                MATCH (p)-[r:POSTED_BY]->(u:SocialUser)
                RETURN p, r, u
            """,
            countQuery = """
                MATCH (p:Post)
                RETURN count(p)
            """)
    Page<Post> findAll(Pageable pageable);

    @Query(value ="""
                MATCH (p:Post)
                WHERE p.isActive = true
                WITH p ORDER BY p.updateDate DESC
                SKIP $skip LIMIT $limit
            
                MATCH (p)-[r:POSTED_BY]->(u:SocialUser)
                RETURN p, r, u
            """,
            countQuery = """
                MATCH (p:Post)
                WHERE p.isActive = true
                RETURN count(p)
            """)
    Page<Post> getFeed(Pageable pageable);
    //endregion

    //region CRUD
    @Query("""
            MATCH (p:Post {id: $id})
            SET p.content = $content,
                p.updateDate = $updateDate,
                p.imageUrl = $imageUrl
            """)
    void update(
            String id,
            String content,
            LocalDateTime updateDate,
            String imageUrl
    );

    @Query("""
            MATCH (p:Post {id: $id_post})
            MATCH (u:SocialUser {id: $id_author})
            MERGE (p)-[:POSTED_BY {postDate: $now}]->(u)
            SET p.updateDate = $now
            """)
    void createPostedRelationship(
            String id_post,
            String id_author,
            LocalDateTime now
    );
    //endregion

    //region LIKE
    @Query("""
            MATCH (u:SocialUser {id: $idUserRequest})
            MATCH (p:Post {id: $idPostToLiked})
            OPTIONAL MATCH(u)-[r:LIKE]->(p)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS following
            """)
    boolean isUserByIdLikedPostById(String idUserRequest, String idPostToLiked);

    @Query("""
            MATCH (u:SocialUser {id: $idUserRequest})
            MATCH (p:Post {id: $idPostToLiked})
            MERGE (u)-[r:LIKE {likeDate: $likeDate}]->(p)
            WITH p
            SET p.likedCount = p.likedCount + 1
            """)
    void createUserByIdLikedPostById(String idUserRequest, String idPostToLiked, LocalDateTime likeDate);

    @Query("""
            MATCH (u:SocialUser {id: $idUserRequest})
            MATCH (p:Post {id: $idPostToLiked})
            MATCH (u)-[r:LIKE]->(p)
            DELETE r
            WITH p
            SET p.likedCount = p.likedCount - 1
            """)
    void deleteUserByIdLikedPostById(String idUserRequest, String idPostToLiked);
    //endregion

    //region validate
    @Query("""
            MATCH (u:SocialUser {id: $idUser})
            MATCH (p:Post {id: $idPost})
            OPTIONAL MATCH(p)-[r:POSTED_BY]->(u)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS isAuthor
            """)
    boolean isUserAuthorOfThePostById(String idUser, String idPost);
    //endregion

    //region TAGGED_WITH
    @Query("""
            MATCH (p:Post {id: $idPost})
            MATCH (t:HashTag {id: $idHashTag})
            MERGE (p)-[:TAGGED_WITH]->(t)
            SET t.postTaggedIn = COALESCE(t.postTaggedIn, 0) + 1
            """)
    void createRelationshipTaggedWithHashTag(String idPost, String idHashTag);

    @Query("""
            MATCH (p:Post {id: $idPost})
            MATCH (p)-[r:TAGGED_WITH]->(t)
            DELETE r
            SET t.postTaggedIn = t.postTaggedIn - 1
            """)
    void deleteAllRelationshipTaggedWithHashTag(String idPost);
    //endregion
}