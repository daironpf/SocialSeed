package com.our.socialseed.user.infrastructure.persistence;

import com.our.socialseed.user.infrastructure.persistence.entity.UserNeo4jEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends Neo4jRepository<UserNeo4jEntity, UUID> {
    Optional<UserNeo4jEntity> findByEmail(String email);
    // Puedes agregar métodos como findByEmail, etc. si quieres
}