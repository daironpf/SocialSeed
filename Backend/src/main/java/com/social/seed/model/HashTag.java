package com.social.seed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Node
public class HashTag{

        @Id
        @GeneratedValue(UUIDStringGenerator.class)
        @Property("identifier")
        private String id;

        private String name;
        private int socialUserInterestIn;
        private int postTaggedIn;
}
