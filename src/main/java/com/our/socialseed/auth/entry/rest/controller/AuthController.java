package com.our.socialseed.auth.entry.rest.controller;

import com.our.socialseed.auth.application.usecase.AuthUseCases;
import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
import com.our.socialseed.auth.entry.rest.dto.LoginRequestDTO;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
import com.our.socialseed.shared.response.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCases authUseCases;
    private final MessageSource messageSource;

    public AuthController(AuthUseCases authUseCases, MessageSource messageSource) {
        this.authUseCases = authUseCases;
        this.messageSource = messageSource;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDTO request, Locale locale) {
        AuthResponseDTO response = authUseCases.login(request.email, request.password);
        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        messageSource.getMessage("auth.login.success", null, locale)
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequestDTO request, Locale locale) {
        AuthResponseDTO response = authUseCases.register(request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        messageSource.getMessage("auth.register.success", null, locale)
                )
        );
    }
}