package com.our.socialseed.user.infrastructure.persistence.entity;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node("User")
public class UserNeo4jEntity {
    @Id
    private UUID id;

    private String username;
    private String email;
    private String password;
    private String fullName;

    // Constructor sin argumentos obligatorio para Neo4j
    public UserNeo4jEntity() {}

    public UserNeo4jEntity(UUID id, String username, String email, String password, String fullName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters y Setters

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }
}
