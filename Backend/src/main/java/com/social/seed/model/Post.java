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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node("Post")
public class Post {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String content;
    private LocalDateTime updateDate;
    private String imageUrl;

    private Boolean isActive;

    @Relationship(type = "POSTED_BY")
    private PostedByRelationship author;
}
