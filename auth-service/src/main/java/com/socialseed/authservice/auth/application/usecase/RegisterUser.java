package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {
    private final AuthService authService;

    public RegisterUser(AuthService authService) {
        this.authService = authService;
    }

    public AuthResponseDTO execute(RegisterRequestDTO dto) {
        return authService.register(dto);
    }
}