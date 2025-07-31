package com.our.socialseed.user.domain.repository;

import com.our.socialseed.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
Este no es el repositorio de Spring Data, sino la interfaz que define lo que el dominio necesita
de cualquier sistema de persistencia. Luego la implementaremos con Neo4j en el adaptador de salida.
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(UUID id);
}
