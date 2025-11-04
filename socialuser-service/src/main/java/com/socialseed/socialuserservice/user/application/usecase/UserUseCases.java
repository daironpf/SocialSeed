package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserUseCases {
    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final GetAllUsers getAllUsers;
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;
    private final ChangeUserPassword changeUserPassword;

    public UserUseCases(
            CreateUser createUser,
            GetUserById getUserById,
            GetAllUsers getAllUsers,
            UpdateUser updateUser,
            DeleteUser deleteUser,
            ChangeUserPassword changeUserPassword
    ) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.getAllUsers = getAllUsers;
        this.updateUser = updateUser;
        this.deleteUser = deleteUser;
        this.changeUserPassword = changeUserPassword;
    }

    public CreateUser createUser() {
        return createUser;
    }

    public GetUserById getUserById() {
        return getUserById;
    }

    public GetAllUsers getAllUsers() {
        return getAllUsers;
    }

    public UpdateUser updateUser() {
        return updateUser;
    }

    public DeleteUser deleteUser() {
        return deleteUser;
    }

    public ChangeUserPassword changeUserPassword() {
        return changeUserPassword;
    }
}
