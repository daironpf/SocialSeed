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
            MATCH (p:Post {identifier: $id})
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
            MATCH (p:Post {identifier: $idpost})
            MATCH (u:SocialUser {identifier: $idauthor})
            MERGE (p)-[:POSTED_BY {postDate: $now}]->(u)
            SET p.updateDate = $now
            """)
    void createPostedRelationship(String idpost, String idauthor, LocalDateTime now);
    //endregion

    //region LIKE
    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (p:Post {identifier: $idPostToLiked})
            OPTIONAL MATCH(u)-[r:LIKE]->(p)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS following
            """)
    boolean isUserByIdLikedPostById(String idUserRequest, String idPostToLiked);

    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (p:Post {identifier: $idPostToLiked})
            MERGE (u)-[r:LIKE {likeDate: $likeDate}]->(p)
            WITH p
            SET p.likedCount = p.likedCount + 1
            """)
    void createUserByIdLikedPostById(String idUserRequest, String idPostToLiked, LocalDateTime likeDate);

    @Query("""
            MATCH (u:SocialUser {identifier: $idUserRequest})
            MATCH (p:Post {identifier: $idPostToLiked})
            MATCH (u)-[r:LIKE]->(p)
            DELETE r
            WITH p
            SET p.likedCount = p.likedCount - 1
            """)
    void deleteUserByIdLikedPostById(String idUserRequest, String idPostToLiked);
    //endregion

    //region validate
    @Query("""
            MATCH (u:SocialUser {identifier: $idUser})
            MATCH (p:Post {identifier: $idPost})
            OPTIONAL MATCH(p)-[r:POSTED_BY]->(u)
            RETURN CASE WHEN r IS NOT NULL THEN true ELSE false END AS isAuthor
            """)
    boolean isUserAuthorOfThePostById(String idUser, String idPost);
    //endregion

    //region TAGGED_WITH
    @Query("""
            MATCH (p:Post {identifier: $idPost})
            MATCH (t:HashTag {identifier: $idHashTag})
            MERGE (p)-[:TAGGED_WITH]->(t)
            SET t.postTaggedIn = COALESCE(t.postTaggedIn, 0) + 1
            """)
    void createRelationshipTaggedWithHashTag(String idPost, String idHashTag);

    @Query("""
            MATCH (p:Post {identifier: $idPost})
            MATCH (p)-[r:TAGGED_WITH]->(t)
            DELETE r
            SET t.postTaggedIn = t.postTaggedIn - 1
            """)
    void deleteAllRelationshipTaggedWithHashTag(String idPost);
    //endregion

    //region Get
    @Query(value ="""
                MATCH (p:Post)-[rp:POSTED_BY]->(u:SocialUser {identifier: $userIdRequest})
                //WHERE p.isActive = true
                WITH p, u, rp ORDER BY rp.postDate DESC
                RETURN p, rp, u
                SKIP $skip LIMIT $limit
            """,
            countQuery = """
                MATCH (p:Post)-[rp:POSTED_BY]->(u:SocialUser {identifier: $userIdRequest})
                //WHERE p.isActive = true
                WITH p, rp ORDER BY rp.postDate DESC
                RETURN count(p)
            """)
    Page<Post> getAllPostsByUserId(String userIdRequest, Pageable pageable);
    //endregion
}