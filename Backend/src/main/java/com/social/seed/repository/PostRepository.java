package com.social.seed.repository;

import com.social.seed.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends Neo4jRepository<Post, String> {

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
}