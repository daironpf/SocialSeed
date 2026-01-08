package com.social.seed.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;

@Data
@RelationshipProperties
public class SocialUserInterestInHashTagRelationShip {
    @RelationshipId
//    @Property(name = "identifier")
    private Long id;

    @Property
    private LocalDateTime interestDate;

    @TargetNode
    private HashTag hashTag;
}
