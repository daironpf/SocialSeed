package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import com.socialseed.authservice.auth.entry.rest.dto.RegisterRequestDTO;
import com.socialseed.authservice.platform.error.BusinessException;
import com.socialseed.authservice.platform.error.ErrorCode;
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

    public AuthServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
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

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto, UUID id) {
        if (authUserRepository.findByEmail(dto.email).isPresent()) {
            throw new BusinessException(ErrorCode.USER_EMAIL_EXISTS, dto.email);
        }

        AuthUser newAuthUser = new AuthUser(
                id,
                dto.username,
                dto.email,
                passwordEncoder.encode(dto.password)
        );

        authUserRepository.save(newAuthUser);

        // si se registró de forma satisfactoria entonces se crea el nodo del SocialUser en Neo4j
        // esto luego pasaría a cuando se verifica el usuario y se activa la cuenta entonces se crearia el nodo en Neo4j

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
        return Optional.empty();
    }

    @Override
    public Optional<AuthUser> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {

    }
}
