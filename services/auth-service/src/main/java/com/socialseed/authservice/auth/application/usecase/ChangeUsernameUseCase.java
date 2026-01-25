package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.application.service.UserSyncService;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChangeUsernameUseCase {

  private static final Logger logger = LoggerFactory.getLogger(ChangeUsernameUseCase.class);

  private final AuthUserRepository authUserRepository;
  private final UserSyncService userSyncService;

  public ChangeUsernameUseCase(AuthUserRepository authUserRepository,
      UserSyncService userSyncService) {
    this.authUserRepository = authUserRepository;
    this.userSyncService = userSyncService;
  }

  @Transactional
  public void execute(UUID userId, String newUsername) {
    // 1. Validar que el usuario existe
    var user = authUserRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (user.getUsername().equals(newUsername)) {
      return; // No change
    }

    // 2. Validar unicidad
    if (authUserRepository.existByUsername(newUsername)) {
      throw new IllegalArgumentException("Username already exists");
    }

    String oldUsername = user.getUsername();

    // 3. Actualizar DB local
    authUserRepository.updateUsername(userId, newUsername);
    logger.info("Username updated in Auth DB for user {}", userId);

    // 4. Sync robusto con SocialUser Service
    userSyncService.syncUsernameChange(userId, oldUsername, newUsername);
  }
}
