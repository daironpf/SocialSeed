package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j;

import com.socialseed.socialuserservice.user.infrastructure.persistence.SpringDataUserRepository;
import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.mapper.UserNeo4jMapper;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Neo4jSocialUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    public Neo4jSocialUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user) {
        var node = UserNeo4jMapper.toNode(user);
        System.out.println("RolesInNode: "+node.getRoles());
        return UserNeo4jMapper.toDomain(springDataUserRepository.save(node));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id)
                .map(UserNeo4jMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(UserNeo4jMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll().stream()
                .map(UserNeo4jMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataUserRepository.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return springDataUserRepository.existByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        return springDataUserRepository.existByUserName(username);
    }

    @Override
    public boolean existByUserId(UUID id) {
        return springDataUserRepository.existByUserId(id);
    }
}
