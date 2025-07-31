package com.our.socialseed.auth.entry.rest.controller;

import com.our.socialseed.auth.application.usecase.AuthUseCases;
import com.our.socialseed.auth.application.usecase.AuthenticateUser;
import com.our.socialseed.auth.application.usecase.RegisterUser;
import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
import com.our.socialseed.auth.entry.rest.dto.LoginRequestDTO;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCases authUseCases;

    public AuthController(AuthUseCases authUseCases) {
        this.authUseCases = authUseCases;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authUseCases.login(request.email, request.password);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        String token = authUseCases.register(request);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
