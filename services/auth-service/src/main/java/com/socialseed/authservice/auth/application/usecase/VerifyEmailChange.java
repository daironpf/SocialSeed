package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.application.service.UserSyncService;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class VerifyEmailChange {

  private final AuthUserRepository authUserRepository;
  private final UserSyncService userSyncService;

  public VerifyEmailChange(AuthUserRepository authUserRepository, UserSyncService userSyncService) {
    this.authUserRepository = authUserRepository;
    this.userSyncService = userSyncService;
  }

  @Transactional
  public void execute(String token) {
    AuthUser user = authUserRepository.findByEmailChangeToken(token)
        .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_TOKEN_INVALID));

    if (user.getEmailChangeTokenExpiry().isBefore(Instant.now())) {
      throw new BusinessException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
    }

    String oldEmail = user.getEmail();
    String newEmail = user.getPendingEmail();

    user.setEmail(newEmail);
    user.setPendingEmail(null);
    user.setEmailChangeToken(null);
    user.setEmailChangeTokenExpiry(null);

    authUserRepository.save(user);

    // Sync robusto con SocialUser Service
    userSyncService.syncEmailChange(user.getId(), oldEmail, newEmail);
  }
}
