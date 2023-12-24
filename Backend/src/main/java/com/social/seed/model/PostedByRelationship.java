package com.social.seed.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;

@Data
@RelationshipProperties
public class PostedByRelationship {

    @RelationshipId
    private Long id;

    @Property
    private LocalDateTime postDate;

    @TargetNode
    private SocialUser author;
}
