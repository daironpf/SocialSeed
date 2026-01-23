package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import com.socialseed.authservice.auth.domain.event.PasswordResetCompletedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class ResetPassword {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public ResetPassword(AuthUserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(String token, String newPassword) {
        Optional<AuthUser> userOptional = userRepository.findByResetPasswordToken(token);

        if (userOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.RESET_TOKEN_INVALID);
        }

        AuthUser user = userOptional.get();

        if (user.getResetPasswordTokenExpiry().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.RESET_TOKEN_EXPIRED);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);
        
        eventPublisher.publishEvent(new PasswordResetCompletedEvent(user.getId()));
    }
}
