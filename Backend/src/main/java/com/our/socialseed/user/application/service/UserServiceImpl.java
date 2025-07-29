package com.our.socialseed.user.application.service;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.port.in.UserService;
import com.our.socialseed.user.domain.port.out.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/*
📌 Notas:
No anotes con @Service aquí: Esta es una clase pura de aplicación (sin Spring). La instancia la conectaremos más adelante desde el adaptador.
El constructor recibe el UserRepository, que luego será implementado (con Neo4j, por ejemplo).
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        // Hashear la contraseña antes de persistir
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        user.setRoles(Set.of("ROLE_USER"));
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
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

    @Override
    public void updateUser(UUID id, User user) {
        userRepository.findById(id).ifPresent(existing -> {
            user.setId(id); // Asegurar que use el mismo ID
            user.setPassword(existing.getPassword()); // Conservamos el password actual
            userRepository.save(user);
        });
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);

        userRepository.save(user);
    }

}