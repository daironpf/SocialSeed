package com.socialseed.socialuserservice.user.domain.service;

import com.socialseed.socialuserservice.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    void updateUser(UUID id, User user);
    void deleteUser(UUID id);
}
