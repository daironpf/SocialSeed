package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class VerifyEmail {

    private final AuthUserRepository authUserRepository;

    public VerifyEmail(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public void execute(String token) {
        AuthUser authUser = authUserRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_TOKEN_INVALID));

        // Check if already verified
        if (authUser.isEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // Check if token is expired
        if (authUser.getVerificationTokenExpiry() == null ||
                authUser.getVerificationTokenExpiry().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
        }

        // Mark email as verified and clear token (single-use)
        authUser.setEmailVerified(true);
        authUser.setVerificationToken(null);
        authUser.setVerificationTokenExpiry(null);

        authUserRepository.save(authUser);
    }
}
