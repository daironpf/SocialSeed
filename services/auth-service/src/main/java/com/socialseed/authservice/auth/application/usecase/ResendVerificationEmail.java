package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.EmailService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ResendVerificationEmail {

    private final AuthUserRepository authUserRepository;
    private final EmailService emailService;

    public ResendVerificationEmail(AuthUserRepository authUserRepository, EmailService emailService) {
        this.authUserRepository = authUserRepository;
        this.emailService = emailService;
    }

    public void execute(String email) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Check if already verified
        if (authUser.isEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // Generate new token and expiry (24 hours)
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(86400); // 24 hours

        authUser.setVerificationToken(token);
        authUser.setVerificationTokenExpiry(expiry);
        authUserRepository.save(authUser);

        // Send verification email
        emailService.sendVerificationEmail(email, token);
    }
}
