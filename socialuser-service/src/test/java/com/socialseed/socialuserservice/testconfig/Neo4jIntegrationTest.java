package com.socialseed.socialuserservice.testconfig;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class Neo4jIntegrationTest {

    static final Neo4jContainer<?> neo4j =
            new Neo4jContainer<>("neo4j:5")
                    .withoutAuthentication();

    @BeforeAll
    static void startContainer() {
        neo4j.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }
}
