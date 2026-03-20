package com.socialseed.socialuserservice.user.application.usecase;

import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.infrastructure.messaging.kafka.KafkaDomainEventPublisher;
import com.socialseed.socialuserservice.user.domain.event.SocialUserProfileUpdatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChangeEmail {
  private final UserRepository userRepository;
  private final KafkaDomainEventPublisher eventPublisher;

  public ChangeEmail(UserRepository userRepository, KafkaDomainEventPublisher eventPublisher) {
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
  }

  @Transactional
  public void execute(UUID userId, String newEmail) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ID, userId));

    user.changeEmail(newEmail);
    userRepository.updateProfile(user);

    eventPublisher.publish(new SocialUserProfileUpdatedEvent(userId));
  }
}
