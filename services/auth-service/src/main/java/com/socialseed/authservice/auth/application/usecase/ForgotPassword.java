package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.EmailService;
import com.socialseed.authservice.auth.domain.event.PasswordResetRequestedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialseed.authservice.auth.domain.util.SecureTokenGenerator;
import java.time.Instant;
import java.util.Optional;

@Service
public class ForgotPassword {

    private final AuthUserRepository userRepository;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    private static final long EXPIRATION_MINUTES = 15;

    public ForgotPassword(AuthUserRepository userRepository, EmailService emailService, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(String email) {
        Optional<AuthUser> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            AuthUser user = userOptional.get();
            String token = SecureTokenGenerator.generate();
            
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(Instant.now().plusSeconds(EXPIRATION_MINUTES * 60));
            
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
            
            eventPublisher.publishEvent(new PasswordResetRequestedEvent(user.getEmail()));
        } else {
            // Log for audit but do not reveal user existence to the caller (effectively handled by controller returning 200)
            // Or maybe we want to send a "account not found" email if we are very nice, but usually silence is safer.
        }
    }
}
