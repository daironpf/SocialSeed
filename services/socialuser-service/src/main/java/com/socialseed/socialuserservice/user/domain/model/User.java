package com.socialseed.socialuserservice.user.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.socialseed.socialuserservice.user.domain.model.valueobject.VacationPeriod;

public class User {

    private final UUID id;
    private String username;
    private String email;
    private String fullName;
    private LocalDate birthDate;
    private UserLanguage language;
    private String profileImage;
    private String bio;

    private UserStatus status;
    private VacationPeriod vacationPeriod;

    private User(UUID id,
                 String username,
                 String email,
                 String fullName,
                 LocalDate birthDate,
                 UserLanguage language,
                 String profileImage,
                 String bio,
                 UserStatus status,
                 VacationPeriod vacationPeriod) {

        this.id = Objects.requireNonNull(id, "id is required");
        this.username = validateRequired(username, "username");
        this.email = validateRequired(email, "email");
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.language = Objects.requireNonNull(language, "language is required");
        this.profileImage = profileImage;
        this.bio = bio;
        this.status = status == null ? UserStatus.ACTIVE : status;
        this.vacationPeriod = vacationPeriod;
    }

    public static User create(String username, String email) {
        return new User(
                UUID.randomUUID(),
                username, email,
                null,
                null,
                UserLanguage.EN,
                null,
                null,
                UserStatus.ACTIVE,
                null
        );
    }

    public void updateProfile(
            String fullName,
            String bio,
            String profileImage,
            LocalDate birthDate,
            UserLanguage language
    ) {

        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("Deleted users cannot be updated");
        }

        this.fullName = fullName;
        this.bio = bio;
        this.profileImage = profileImage;

        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthDate cannot be in the future");
        }
        this.birthDate = birthDate;

        this.language = Objects.requireNonNull(language, "language is required");
    }

    /* The goal is to reconstruct a domain entity from persisted data without triggering creation rules or validation logic that are only meant for new users.

        Example scenario:
        You have a UserNeo4jEntity loaded from Neo4j.
        You need a User instance for your domain logic or use cases.
        You cannot use User.create(...) because that generates a new ID and applies business rules for a new user.
        rehydrate allows you to:
        Pass the existing ID, status, language, etc.
        Restore the entity exactly as it exists in the database.
        Keep your domain pure and consistent.
    */
    public static User rehydrate(
            UUID id,
            String username,
            String email,
            String fullName,
            LocalDate birthDate,
            UserLanguage language,
            String profileImage,
            String bio,
            UserStatus status,
            VacationPeriod vacationPeriod
        ) {
        return new User(
            id,
            username,
            email,
            fullName,
            birthDate,
            language,
            profileImage,
            bio,
            status,
            vacationPeriod
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

    public void goOnVacation(VacationPeriod period) {
        if (status != UserStatus.ACTIVE) {
            throw new IllegalStateException("User must be ACTIVE to go on vacation.");
        }
        this.vacationPeriod = Objects.requireNonNull(period, "vacationPeriod is required");
        this.status = UserStatus.ON_VACATION;
    }

    public void returnFromVacation() {
        if (status != UserStatus.ON_VACATION) {
            throw new IllegalStateException("User is not on vacation.");
        }
        this.status = UserStatus.ACTIVE;
        this.vacationPeriod = null;
    }

    public void goDeactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void reActive(){
        this.status = UserStatus.ACTIVE;
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

    public void changeLanguage(UserLanguage newLanguage) {
        this.language = Objects.requireNonNull(newLanguage, "language is required");
    }

    // Getters
    public UUID getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getFullName() { return this.fullName; }
    public LocalDate getBirthDate() { return this.birthDate; }
    public String getBio() { return this.bio; }
    public UserStatus getStatus() { return this.status; }
    public String getEmail() { return this.email; }
    public UserLanguage getLanguage() { return this.language; }
    public String getProfileImage() { return this.profileImage; }
    public VacationPeriod getVacationPeriod() { return this.vacationPeriod; }
}