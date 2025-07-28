package com.our.socialseed.user.adapter.out.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

public interface SpringDataUserRepository extends Neo4jRepository<UserNode, UUID> {
    // Puedes agregar métodos como findByEmail, etc. si quieres
}