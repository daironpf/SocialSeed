package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SocialUserRepository extends Neo4jRepository<SocialUser, String> {
}
