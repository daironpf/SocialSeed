package com.our.socialseed.user.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/*
📌 Consideraciones
Esta clase representa tu modelo de dominio puro, sin anotaciones de Spring ni persistencia.
Más adelante definiremos un UserNode o UserEntity para mapear a Neo4j en el adaptador de salida.
 */
public class User {
    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String fullName;

    public User(UUID id, String username, String email, String password, String fullName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
