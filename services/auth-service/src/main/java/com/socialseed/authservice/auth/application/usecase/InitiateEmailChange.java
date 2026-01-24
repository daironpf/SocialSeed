package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.EmailService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class InitiateEmailChange {

  private final AuthUserRepository authUserRepository;
  private final EmailService emailService;

  public InitiateEmailChange(AuthUserRepository authUserRepository, EmailService emailService) {
    this.authUserRepository = authUserRepository;
    this.emailService = emailService;
  }

  @Transactional
  public void execute(UUID userId, String newEmail) {
    AuthUser user = authUserRepository.findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if (authUserRepository.existByEmail(newEmail)) {
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, newEmail);
    }

    String token = UUID.randomUUID().toString();
    user.setPendingEmail(newEmail);
    user.setEmailChangeToken(token);
    user.setEmailChangeTokenExpiry(Instant.now().plusSeconds(3600)); // 1 hour

    authUserRepository.save(user);

    emailService.sendVerificationEmail(newEmail, token);
  }
}
