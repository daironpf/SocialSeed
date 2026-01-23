package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.LoginAttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final AuthUserRepository authUserRepository;

    public LoginAttemptServiceImpl(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedLogin(UUID userId) {
        AuthUser authUser = authUserRepository.findById(userId).orElseThrow();
        
        authUser.setFailedLoginAttempts(authUser.getFailedLoginAttempts() + 1);
        authUser.setLastFailedLoginAt(Instant.now());
        
        // Lock account after 5 failed attempts
        if (authUser.getFailedLoginAttempts() >= 5) {
            authUser.setAccountNonLocked(false);
        }
        
        authUserRepository.save(authUser);
    }

    @Override
    @Transactional
    public void recordSuccessfulLogin(UUID userId) {
        AuthUser authUser = authUserRepository.findById(userId).orElseThrow();
        
        authUser.setLastLoginAt(Instant.now());
        authUser.setFailedLoginAttempts(0);
        authUser.setLastFailedLoginAt(null);
        
        authUserRepository.save(authUser);
    }
}
