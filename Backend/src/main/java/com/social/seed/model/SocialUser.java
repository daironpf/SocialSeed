package com.social.seed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node("SocialUser")
public class SocialUser {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private LocalDateTime dateBorn;
    private LocalDateTime registrationDate;

    private String fullName;
    private String userName;
    private String email;
    private String language;

    private Boolean onVacation;
    private Boolean isActive;

}
