package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.StartVacationRequestDTO;
import com.socialseed.socialuserservice.user.entry.rest.dto.request.UpdateUserProfileDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserUseCases {
    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final GetUserByName getUserByName;
    private final GetUserByEmail getUserByEmail;
    private final GetAllUsers getAllUsers;
    private final UpdateUserProfile updateUserProfile;
    private final DeleteUser deleteUser;
    private final StartVacation startVacation;
    private final EndVacation endVacation;
    private final ChangeUsername changeUsername;
    private final ChangeEmail changeEmail;

    public UserUseCases(
            CreateUser createUser,
            GetUserById getUserById,
            GetUserByName getUserByName,
            GetUserByEmail getUserByEmail,
            GetAllUsers getAllUsers,
            UpdateUserProfile updateUserProfile,
            DeleteUser deleteUser,
            StartVacation startVacation,
            EndVacation endVacation,
            ChangeUsername changeUsername,
            ChangeEmail changeEmail) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.getUserByName = getUserByName;
        this.getUserByEmail = getUserByEmail;
        this.getAllUsers = getAllUsers;
        this.updateUserProfile = updateUserProfile;
        this.deleteUser = deleteUser;
        this.startVacation = startVacation;
        this.endVacation = endVacation;
        this.changeUsername = changeUsername;
        this.changeEmail = changeEmail;
    }

    public Optional<User> getUserById(UUID id) {
        return getUserById.execute(id);
    }

    public Optional<User> getUserByName(String userName) {
        return getUserByName.execute(userName);
    }

    public Optional<User> getUserByEmail(String email) {
        return getUserByEmail.execute(email);
    }

    public List<User> getAllUsers() {
        return getAllUsers.execute();
    }

    public User createUser(User user) {
        return createUser.execute(user);
    }

    public void updateUserProfile(UpdateUserProfileDTO request) {
        updateUserProfile.execute(request);
    }

    public void deleteUser(UUID id) {
        deleteUser.execute(id);
    }

    public void startVacation(StartVacationRequestDTO request) {
        startVacation.execute(request);
    }

    public void endVacation(UUID userId) {
        endVacation.execute(userId);
    }

    public void changeUsername(UUID userId, String newUsername) {
        changeUsername.execute(userId, newUsername);
    }

    public void changeEmail(UUID userId, String newEmail) {
        changeEmail.execute(userId, newEmail);
    }
}