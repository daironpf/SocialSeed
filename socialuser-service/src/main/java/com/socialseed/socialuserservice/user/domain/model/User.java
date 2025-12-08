package com.socialseed.socialuserservice.user.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private String username;
    private String email;
    private String fullName;
    private LocalDate birthDate;
    private String language;
    private String profileImage;
    private String bio;

    private UserStatus status;

    private User(UUID id,
                 String username,
                 String email,
                 String fullName,
                 LocalDate birthDate,
                 String language,
                 String profileImage,
                 String bio,
                 UserStatus status) {

        this.id = Objects.requireNonNull(id, "id is required");
        this.username = validateRequired(username, "username");
        this.email = validateRequired(email, "email");
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.language = language;
        this.profileImage = profileImage;
        this.bio = bio;
        this.status = status == null ? UserStatus.ACTIVE : status;
    }

    public static User create(String username, String email) {
        return new User(
                UUID.randomUUID(),
                username, email,
                null,
                null,
                null,
                null,
                null,
                UserStatus.ACTIVE
        );
    }

    private static String validateRequired(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be empty");
        }
        return value;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updateFullName(String fullName) {
        this.fullName = fullName;
    }

    public void goOnVacation() {
        if (status != UserStatus.ACTIVE) {
            throw new IllegalStateException("User must be ACTIVE to go on vacation.");
        }
        this.status = UserStatus.ON_VACATION;
    }

    public void returnFromVacation() {
        if (status != UserStatus.ON_VACATION) {
            throw new IllegalStateException("User is not on vacation.");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    public void changeUsername(String newUsername) {
        this.username = validateRequired(newUsername, "username");
    }

    public void updateBirthDate(LocalDate newBirthDate) {
        if (newBirthDate != null && newBirthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthDate cannot be in the future");
        }
        this.birthDate = newBirthDate;
    }


    // Getters
    public UUID getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getFullName() { return this.fullName; }
    public LocalDate getBirthDate() { return this.birthDate; }
    public String getBio() { return this.bio; }
    public UserStatus getStatus() { return this.status; }
    public String getEmail() { return this.email; }
}