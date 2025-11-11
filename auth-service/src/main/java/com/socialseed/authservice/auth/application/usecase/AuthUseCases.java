package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;

//Orquestador de casos de uso (AuthUseCases)
@Service
public class AuthUseCases {
    private final AuthenticateUser authenticateUser;
    private final RegisterUser registerUser;

    public AuthUseCases(AuthService authService) {
        this.authenticateUser = new AuthenticateUser(authService);
        this.registerUser = new RegisterUser(authService);
    }

    public AuthResponseDTO login(String email, String password) {
        return authenticateUser.execute(email, password);
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        return registerUser.execute(dto);
    }
}
