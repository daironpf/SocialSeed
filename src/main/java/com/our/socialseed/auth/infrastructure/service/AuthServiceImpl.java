package com.our.socialseed.auth.infrastructure.service;

import com.our.socialseed.auth.config.exception.EmailAlreadyExistsException;
import com.our.socialseed.auth.domain.service.AuthService;
import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;
//import com.our.socialseed.auth.infrastructure.security.JWTProvider;
import com.our.socialseed.shared.security.jwt.JWTProvider;

import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

//    @Override
//    public String login(String email, String password) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//        return jwtProvider.generateToken(optionalUser.get().getUsername());
//    }

    @Override
    public AuthResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtProvider.generateToken(user.getUsername());
        Set<String> roles = user.getRoles(); // asumiendo que tu entidad User tiene un campo roles

        return new AuthResponseDTO(token, roles);
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email).isPresent()) {
            throw new EmailAlreadyExistsException(); // ya no usamos RuntimeException genérico
        }

        User newUser = new User(
                UUID.randomUUID(),
                dto.username,
                dto.email,
                passwordEncoder.encode(dto.password),
                dto.fullName
        );

        userRepository.save(newUser);

        String token = jwtProvider.generateToken(newUser.getUsername());
        Set<String> roles = newUser.getRoles(); // asumiendo que tu entidad User tiene un campo roles

        return new AuthResponseDTO(token, roles);
    }
}
