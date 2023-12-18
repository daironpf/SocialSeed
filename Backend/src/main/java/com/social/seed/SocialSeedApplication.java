package com.social.seed;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Social Seed",
				description = "Social Seed is a seed from which you will grow your Social Network",
				version = "0.0.1",
				contact = @Contact(
						name = "Dairon Pérez Frías",
						email = "dairon.perezfrias@gmail.com",
						url = "https://github.com/daironpf"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
		)
)
public class SocialSeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialSeedApplication.class, args);
	}

}