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
package com.social.seed;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableAspectJAutoProxy
@EnableNeo4jRepositories
@SpringBootApplication
@ComponentScan(basePackages = "com.social.seed")
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