package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import org.springframework.stereotype.Service;

//Orquestador de casos de uso (AuthUseCases)
@Service
public class AuthUseCases {
    private final AuthenticateUser authenticateUser;
    private final RegisterUser registerUser;
    private final SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient;

    public AuthUseCases(AuthenticateUser authenticateUser, RegisterUser registerUser, SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient) {
        this.authenticateUser = authenticateUser;
        this.registerUser = registerUser;
        this.socialUserClient = socialUserClient;
    }

    public AuthResponseDTO login(String email, String password) {
        return authenticateUser.execute(email, password);
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        return registerUser.execute(dto);
    }
}
