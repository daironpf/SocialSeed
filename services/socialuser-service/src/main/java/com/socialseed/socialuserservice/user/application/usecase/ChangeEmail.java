package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository.SocialUserNeo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChangeEmail {
  private final SocialUserNeo4jRepository repository;

  public ChangeEmail(SocialUserNeo4jRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(UUID userId, String newEmail) {
    if (!repository.existsById(userId)) {
      throw new IllegalArgumentException("User not found: " + userId);
    }

    repository.updateEmail(userId, newEmail);
  }
}
