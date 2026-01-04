package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
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

    public UserUseCases(
            CreateUser createUser,
            GetUserById getUserById,
            GetUserByName getUserByName,
            GetUserByEmail getUserByEmail,
            GetAllUsers getAllUsers,
            UpdateUserProfile updateUserProfile,
            DeleteUser deleteUser
    ) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.getUserByName = getUserByName;
        this.getUserByEmail = getUserByEmail;
        this.getAllUsers = getAllUsers;
        this.updateUserProfile = updateUserProfile;
        this.deleteUser = deleteUser;
    }

    public Optional<User> getUserById(UUID id) {
        return getUserById.execute(id);
    }
    public Optional<User> getUserByName(String userName) { return getUserByName.execute(userName); }
    public Optional<User> getUserByEmail(String email) { return getUserByEmail.execute(email); }
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
}