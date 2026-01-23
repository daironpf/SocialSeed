package com.socialseed.authservice.auth.infrastructure.email;

import com.socialseed.authservice.auth.domain.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StubEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(StubEmailService.class);

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        logger.info("==================================================");
        logger.info("STUB EMAIL SERVICE - SENDING PASSWORD RESET EMAIL");
        logger.info("To: {}", to);
        logger.info("Token: {}", token);
        logger.info("Link: https://socialseed.com/reset-password?token={}", token);
        logger.info("==================================================");
    }

    @Override
    public void sendVerificationEmail(String to, String token) {
        logger.info("==================================================");
        logger.info("STUB EMAIL SERVICE - SENDING VERIFICATION EMAIL");
        logger.info("To: {}", to);
        logger.info("Token: {}", token);
        logger.info("Link: https://socialseed.com/verify-email?token={}", token);
        logger.info("==================================================");
    }
}
