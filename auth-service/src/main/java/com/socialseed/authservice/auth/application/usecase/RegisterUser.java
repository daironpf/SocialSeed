package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AuthResponseDTO execute(RegisterRequestDTO dto) {

        var request = com.socialseed.socialuserservice.proto.CreateUserRequest.newBuilder()
                .setUsername(dto.username)
                .setEmail(dto.email)
                .build();

        var response = socialUserClient.createUser(request);

        if ("200".equals(response.getMessage())){
            return authService.register(dto, UUID.fromString(response.getUserId()));
        }
        return null;
    }
}