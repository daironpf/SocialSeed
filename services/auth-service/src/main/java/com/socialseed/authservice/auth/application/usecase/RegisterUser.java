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
import org.springframework.grpc.client.*;

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

        // 1️Crar el usuario social vía gRPC
        var request = com.socialseed.contracts.socialuser.CreateUserRequest.newBuilder()
                .setUsername(authUser.getUsername())
                .setEmail(authUser.getEmail())
                .build();

        var response = socialUserClient.createUser(request);

        if (!"200".equals(response.getMessage())) {
            log.error("Failed to create user in SocialUserService: {}", response.getMessage());
            throw new BusinessException(ErrorCode.USER_CREATION_FAILED, authUser.getEmail());
        }

        UUID socialUserId = UUID.fromString(response.getUserId());

        // 2️⃣ Registrar usuario en AuthService
        return authService.register(authUser, socialUserId);
    }
}