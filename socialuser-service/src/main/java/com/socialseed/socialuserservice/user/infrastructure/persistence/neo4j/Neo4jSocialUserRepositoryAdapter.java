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
    private final UserNeo4jMapper userNeo4jMapper;

    public Neo4jSocialUserRepositoryAdapter(SocialUserNeo4jRepository socialUserNeo4jRepository,
                                            UserNeo4jMapper userNeo4jMapper) {
        this.socialUserNeo4jRepository = socialUserNeo4jRepository;
        this.userNeo4jMapper = userNeo4jMapper;
    }

    @Override
    public User save(User user) {
        var entity = userNeo4jMapper.toEntity(user);
        var savedEntity = socialUserNeo4jRepository.save(entity);
        return userNeo4jMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return socialUserNeo4jRepository.findById(id)
                .map(userNeo4jMapper::toDomain);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return socialUserNeo4jRepository.findByUsername(userName)
                .map(userNeo4jMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return socialUserNeo4jRepository.findByEmail(email)
                .map(userNeo4jMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return socialUserNeo4jRepository.findAll().stream()
                .map(userNeo4jMapper::toDomain)
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
