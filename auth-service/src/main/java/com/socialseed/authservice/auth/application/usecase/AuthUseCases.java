package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

//Orquestador de casos de uso (AuthUseCases)
@Service
public class AuthUseCases {
    private final AuthenticateUser authenticateUser;
    private final RegisterUser registerUser;
    public final ChangeUserPassword changeUserPassword;

    public AuthUseCases(AuthenticateUser authenticateUser, RegisterUser registerUser, ChangeUserPassword changeUserPassword) {
        this.authenticateUser = authenticateUser;
        this.registerUser = registerUser;
        this.changeUserPassword = changeUserPassword;
    }

    public AuthResponseDTO login(String email, String password) {
        return authenticateUser.execute(email, password);
    }

    public AuthResponseDTO register(AuthUser authUser) {
        return registerUser.execute(authUser);
    }

    public AuthResponseDTO changeUserPassword(UUID userId, String currentPassword, String newPassword) {
        return changeUserPassword.execute(userId, currentPassword, newPassword);
    }
}
