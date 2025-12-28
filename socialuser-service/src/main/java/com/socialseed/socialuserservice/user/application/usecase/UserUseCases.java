package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserUseCases {
    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final GetUserByName getUserByName;
    private final GetAllUsers getAllUsers;
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;


    public UserUseCases(
            CreateUser createUser,
            GetUserById getUserById,
            GetUserByName getUserByName,
            GetAllUsers getAllUsers,
            UpdateUser updateUser,
            DeleteUser deleteUser
    ) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.getUserByName = getUserByName;
        this.getAllUsers = getAllUsers;
        this.updateUser = updateUser;
        this.deleteUser = deleteUser;
    }

    public User createUser(User user) {
        return createUser.execute(user);
    }

    public Optional<User> getUserById(UUID id) {
        return getUserById.execute(id);
    }
    public Optional<User> getUserByName(String userName) {
        return getUserByName.execute(userName);
    }

    public List<User> getAllUsers() {
        return getAllUsers.execute();
    }

    public void updateUser(UUID id,User request) {
        updateUser.execute(id, request);
    }

    public void deleteUser(UUID id) {
        deleteUser.execute(id);
    }
}