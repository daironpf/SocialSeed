package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterUser {

    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient;

    public RegisterUser(AuthService authService, SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient) {
        this.authService = authService;
        this.socialUserClient = socialUserClient;
    }

    public AuthResult execute(AuthUser authUser) {
        log.info("Starting user registration for email: {}", authUser.getEmail());

        UUID socialUserId = UUID.randomUUID();
        log.info("Created temporary socialUserId for testing: {}", socialUserId);

        try {
            var request = com.socialseed.contracts.socialuser.CreateUserRequest.newBuilder()
                    .setUsername(authUser.getUsername())
                    .setEmail(authUser.getEmail())
                    .build();

            log.info("Calling socialuser-service gRPC...");
            var response = socialUserClient.createUser(request);
            log.info("SocialUserService response received: message={}, userId={}", response.getMessage(), response.getUserId());

            if (response.getUserId() == null || response.getUserId().isBlank()) {
                log.error("SocialUserService returned empty userId. Message: {}", response.getMessage());
                throw new BusinessException(ErrorCode.USER_CREATION_FAILED, authUser.getEmail());
            }

            socialUserId = UUID.fromString(response.getUserId());
            log.info("Social user created with ID: {}", socialUserId);

        } catch (BusinessException e) {
            log.error("Business exception during registration: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("gRPC call to SocialUserService failed: {} - {}. Registration cannot continue.",
                    e.getClass().getName(), e.getMessage());
            throw new BusinessException(ErrorCode.USER_CREATION_FAILED, authUser.getEmail());
        }

        log.info("Creating auth user with socialUserId: {}", socialUserId);
        AuthResult result = authService.register(authUser, socialUserId);
        log.info("Registration complete: {}", result);
        return result;
    }
}