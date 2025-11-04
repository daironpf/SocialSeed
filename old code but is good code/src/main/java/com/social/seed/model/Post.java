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
@Node("Post")
public class Post {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("identifier")
    private String id;

    private String content;
    private LocalDateTime updateDate;
    private String imageUrl;

    private Boolean isActive;

    private int likedCount;

    @Relationship(type = "POSTED_BY")
    private PostedByRelationship author;

    @Relationship(type = "TAGGED_WITH")
    private List<HashTag> hashTags;
}