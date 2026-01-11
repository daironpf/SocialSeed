package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Node("SocialUser")
public class UserNeo4jEntity {

    @Id
    private UUID id;

    @Property("username")
    private String username;

    @Property("email")
    private String email;

    @Property("full_name")
    private String fullName;

    @Property("birth_date")
    private LocalDate birthDate;

    /**
     * Stored as String:
     * EN, ES, etc.
     */
    @Property("language")
    private String language;

    @Property("profile_image")
    private String profileImage;

    @Property("bio")
    private String bio;

    /**
     * ACTIVE, INACTIVE, ON_VACATION, DELETED
     */
    @Property("status")
    private String status;

    // === Constructors ===

    public UserNeo4jEntity() {
    }

    public UserNeo4jEntity(
            UUID id,
            String username,
            String email,
            String fullName,
            LocalDate birthDate,
            String language,
            String profileImage,
            String bio,
            String status
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.language = language;
        this.profileImage = profileImage;
        this.bio = bio;
        this.status = status;
    }

    // === Getters & Setters ===

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // === Builder ===

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID id;
        private String username;
        private String email;
        private String fullName;
        private LocalDate birthDate;
        private String language;
        private String profileImage;
        private String bio;
        private String status;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public UserNeo4jEntity build() {
            return new UserNeo4jEntity(
                    id,
                    username,
                    email,
                    fullName,
                    birthDate,
                    language,
                    profileImage,
                    bio,
                    status
            );
        }
    }

    // === equals & hashCode ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserNeo4jEntity)) return false;
        UserNeo4jEntity that = (UserNeo4jEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(language, that.language) &&
                Objects.equals(profileImage, that.profileImage) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                username,
                email,
                fullName,
                birthDate,
                language,
                profileImage,
                bio,
                status
        );
    }

    // === toString ===

    @Override
    public String toString() {
        return "UserNeo4jEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", language='" + language + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", bio='" + bio + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
