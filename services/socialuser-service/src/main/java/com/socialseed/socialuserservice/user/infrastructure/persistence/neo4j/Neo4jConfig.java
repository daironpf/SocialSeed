package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository")
public class Neo4jConfig {
}
