package com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository.SocialUserNeo4jRepository;
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

    private final SocialUserNeo4jRepository socialUserNeo4jRepository;

    public Neo4jSocialUserRepositoryAdapter(SocialUserNeo4jRepository springDataUserRepository) {
        this.socialUserNeo4jRepository = springDataUserRepository;
    }

    @Override
    public User save(User user) {
        var node = UserNeo4jMapper.toNode(user);
        return UserNeo4jMapper.toDomain(socialUserNeo4jRepository.save(node));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return socialUserNeo4jRepository.findById(id)
                .map(UserNeo4jMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return socialUserNeo4jRepository.findByEmail(email)
                .map(UserNeo4jMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return socialUserNeo4jRepository.findAll().stream()
                .map(UserNeo4jMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        socialUserNeo4jRepository.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return socialUserNeo4jRepository.existByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        return socialUserNeo4jRepository.existByUserName(username);
    }

    @Override
    public boolean existByUserId(UUID id) {
        return socialUserNeo4jRepository.existByUserId(id);
    }
}
