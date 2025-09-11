package com.our.socialseed.user.application.usecase;

import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUseCases {
    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final GetAllUsers getAllUsers;
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;
    private final ChangeUserPassword changeUserPassword;

    public UserUseCases(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.createUser = new CreateUser(userRepository, passwordEncoder);
        this.getUserById = new GetUserById(userRepository);
        this.getAllUsers = new GetAllUsers(userRepository);
        this.updateUser = new UpdateUser(userRepository);
        this.deleteUser = new DeleteUser(userRepository);
        this.changeUserPassword = new ChangeUserPassword(userRepository, passwordEncoder);
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
