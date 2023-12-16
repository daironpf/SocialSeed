package com.social.seed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories
@SpringBootApplication
public class SocialSeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialSeedApplication.class, args);
	}

}