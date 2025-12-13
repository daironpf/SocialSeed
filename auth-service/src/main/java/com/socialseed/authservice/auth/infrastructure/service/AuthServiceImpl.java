package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import com.socialseed.authservice.platform.error.BusinessException;
import com.socialseed.authservice.platform.error.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final UserRegisteredEventPublisher eventPublisher;

    public AuthServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider, UserRegisteredEventPublisher eventPublisher) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public AuthResponseDTO login(String email, String password) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, authUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtProvider.generateToken(authUser.getUsername());
        Set<String> roles = authUser.getRoles(); // asumiendo que tu entidad User tiene un campo roles

        return new AuthResponseDTO(token, roles);
    }

    @Transactional
    @Override
    public AuthResponseDTO register(AuthUser authUser, UUID id) {

        AuthUser newAuthUser = new AuthUser(
                id,
                authUser.getUsername(),
                authUser.getEmail(),
                passwordEncoder.encode(authUser.getPassword())
        );

        authUserRepository.save(newAuthUser);

        // Emit to Kafka Server
        UserRegisteredEvent event = new UserRegisteredEvent(
                id,
                authUser.getEmail(),
                authUser.getEmail(),
                System.currentTimeMillis()
        );
        eventPublisher.publish(event);

        // Generate Token
        String token = jwtProvider.generateToken(newAuthUser.getUsername());
        Set<String> roles = newAuthUser.getRoles(); // asumiendo que tu entidad User tiene un campo roles

        return new AuthResponseDTO(token, roles);
    }

    @Override
    public AuthUser createUser(AuthUser authUser) {
        return null;
    }

    @Override
    public Optional<AuthUser> getUserById(UUID id) {
        return authUserRepository.findById(id);
    }

    @Override
    public Optional<AuthUser> getUserByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public boolean existByUserId(UUID id) {
        return authUserRepository.existByUserId(id);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {


    }
}
