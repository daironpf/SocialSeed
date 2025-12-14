package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node("SocialUser")
public class UserNeo4jEntity {

    @Id
    private UUID id;

    @Property("username")
    private String username;

    @Property("email")
    private String email;

    @Property("full_name")
    private String fullName;

    @Property("birth_date")
    private LocalDate birthDate;

    /**
     * Stored as String:
     * EN, ES, etc.
     */
    @Property("language")
    private String language;

    @Property("profile_image")
    private String profileImage;

    @Property("bio")
    private String bio;

    /**
     * ACTIVE, INACTIVE, ON_VACATION, DELETED
     */
    @Property("status")
    private String status;
}