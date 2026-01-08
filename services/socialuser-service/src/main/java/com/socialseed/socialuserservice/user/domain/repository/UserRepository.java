package com.socialseed.socialuserservice.user.domain.repository;

import com.socialseed.socialuserservice.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/*
Este no es el repositorio de Spring Data, sino la interfaz que define lo que el dominio necesita
de cualquier sistema de persistencia. Luego la implementaremos con Neo4j en el adaptador de salida.
 */
public interface UserRepository {
    User save(User user);
    void updateProfile(User user);

    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    List<User> findAll();

    void deleteById(UUID id);

    boolean existByEmail(String email);
    boolean existByUsername(String username);
    boolean existByUserId(UUID id);
}