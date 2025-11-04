package com.social.seed.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;

@Data
@RelationshipProperties
public class PostedByRelationship {

    @RelationshipId
//    @Property("identifier")
    private Long id;

    @Property
    private LocalDateTime postDate;

    @TargetNode
    private SocialUser author;
}
