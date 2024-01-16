package com.social.seed.repository;

import com.social.seed.model.HashTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface HashTagRepository extends Neo4jRepository<HashTag, String> {

    @Query("""
            OPTIONAL MATCH (t:HashTag)
            WHERE toLower(t.name) = toLower($name)
            RETURN CASE WHEN t IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existByName(String name);

    @Query("""
            OPTIONAL MATCH (t:HashTag {identifier:$id})
            RETURN CASE WHEN t IS NOT NULL THEN true ELSE false END AS existUser
            """)
    Boolean existById(String id);

    @Query("""
            MATCH (t:HashTag {identifier:$id})
            SET t.name = $name
            """)
    void update(String id, String name);

    @Query("""
            MATCH (t:HashTag)
            WHERE toLower(t.name) = toLower($name)
            RETURN t
            """)
    Optional<HashTag> findByName(String name);
}
