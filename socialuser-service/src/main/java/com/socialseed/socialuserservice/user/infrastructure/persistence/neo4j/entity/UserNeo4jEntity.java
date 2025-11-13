package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node("SocialUser")
public class UserNeo4jEntity {
    @Id
    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String email;
    private String fullName;
}