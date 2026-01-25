package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.repository.SocialUserNeo4jRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChangeUsername {
  private final SocialUserNeo4jRepository repository;

  public ChangeUsername(SocialUserNeo4jRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(UUID userId, String newUsername) {
    if (!repository.existsById(userId)) {
      // Note: In a real sync scenario, we might want to create IT if it doesn't
      // exist,
      // but for a "change" operation, expecting existence is reasonable.
      throw new IllegalArgumentException("User not found: " + userId);
    }

    // Check uniqueness in Neo4j (optional but good for consistency)
    if (repository.existByUserName(newUsername)) {
      // In distributed system, Auth is source of truth for username uniqueness.
      // If we are here, Auth already updated. If current Neo4j user has this name,
      // it's fine (idempotency).
      // If ANOTHER user has it, we have a sync issue.
      // For now, let's just update blindly or log warning.
      // Ideally we shouldn't throw error to block Auth service if it's already done
      // there.
    }

    repository.updateUsername(userId, newUsername);
  }
}
