package com.our.socialseed.user.domain.service;

import com.our.socialseed.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
✍️ Más adelante puedes extender este puerto con métodos como updateUser, deleteUser, changePassword, etc.
 */
public interface UserService {

    User createUser(User user);
    Optional<User> getUserById(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, User user);
    void deleteUser(UUID id);
    void changePassword(UUID userId, String currentPassword, String newPassword);

}
