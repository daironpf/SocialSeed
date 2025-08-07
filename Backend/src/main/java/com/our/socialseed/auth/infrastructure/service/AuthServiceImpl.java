package com.our.socialseed.auth.infrastructure.service;

import com.our.socialseed.auth.domain.service.AuthService;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
//import com.our.socialseed.auth.infrastructure.security.JWTProvider;
import com.our.socialseed.shared.security.jwt.JWTProvider;
import com.our.socialseed.shared.security.jwt.JwtAuthFilter;

import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtProvider.generateToken(optionalUser.get().getUsername());
    }

    @Override
    public String register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User newUser = new User(
                UUID.randomUUID(),
                dto.username,
                dto.email,
                passwordEncoder.encode(dto.password),
                dto.fullName
        );

        userRepository.save(newUser);

        return jwtProvider.generateToken(newUser.getUsername());
    }
}
