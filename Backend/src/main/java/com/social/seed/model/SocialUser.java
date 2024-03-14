package com.social.seed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node
public class SocialUser {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("identifier")
    private String id;

    private LocalDateTime dateBorn;
    private LocalDateTime registrationDate;

    private String fullName;
    private String userName;
    private String email;
    private String language;
    private String profileImage;
    private String bio;

    private Boolean onVacation;
    private Boolean isActive;
    private Boolean isDeleted;

    private Integer friendCount;
    private Integer followersCount;
    private Integer followingCount;
    private Integer friendRequestCount;

    @Relationship(type = "INTERESTED_IN_HASHTAG")
    private List<SocialUserInterestInHashTagRelationShip> hashTags;
}
