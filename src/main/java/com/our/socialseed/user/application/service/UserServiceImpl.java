package com.our.socialseed.user.application.service;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.port.in.UserService;
import com.our.socialseed.user.domain.port.out.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
📌 Notas:
No anotes con @Service aquí: Esta es una clase pura de aplicación (sin Spring). La instancia la conectaremos más adelante desde el adaptador.
El constructor recibe el UserRepository, que luego será implementado (con Neo4j, por ejemplo).
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}