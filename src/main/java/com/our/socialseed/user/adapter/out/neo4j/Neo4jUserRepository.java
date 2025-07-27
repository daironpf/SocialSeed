package com.our.socialseed.user.adapter.out.neo4j;

import com.our.socialseed.user.adapter.out.neo4j.mapper.UserNeo4jMapper;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.port.out.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Neo4jUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    public Neo4jUserRepository(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user) {
        var node = UserNeo4jMapper.toNode(user);
        return UserNeo4jMapper.toDomain(springDataUserRepository.save(node));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id)
                .map(UserNeo4jMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll().stream()
                .map(UserNeo4jMapper::toDomain)
                .collect(Collectors.toList());
    }
}
