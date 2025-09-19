package com.our.socialseed.auth.application.usecase;

import com.our.socialseed.auth.domain.service.AuthService;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {
    private final AuthService authService;

    public RegisterUser(AuthService authService) {
        this.authService = authService;
    }

    public String execute(RegisterRequestDTO dto) {
        return authService.register(dto);
    }
}
