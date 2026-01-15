package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.entry.rest.dto.AuthResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthUseCases {
    private final AuthenticateUser authenticateUser;
    private final RegisterUser registerUser;
    public final ChangeUserPassword changeUserPassword;
    private final GetUserById getUserById;
    private final GetAuthUserByEmail getAuthUserByEmail;
    private final GetAuthUserByUserName getAuthUserByUserName;
    private final Logout logout;

    public AuthUseCases(
            AuthenticateUser authenticateUser,
            RegisterUser registerUser,
            ChangeUserPassword changeUserPassword,
            GetUserById getUserById,
            GetAuthUserByEmail getAuthUserByEmail,
            GetAuthUserByUserName getAuthUserByUserName,
            Logout logout) {
        this.authenticateUser = authenticateUser;
        this.registerUser = registerUser;
        this.changeUserPassword = changeUserPassword;
        this.getUserById = getUserById;
        this.getAuthUserByEmail = getAuthUserByEmail;
        this.getAuthUserByUserName = getAuthUserByUserName;
        this.logout = logout;
    }

    public Optional<AuthUser> getAuthUserById(UUID userId) {
        return getUserById.execute(userId);
    }

    public Optional<AuthUser> getAuthUserByEmail(@Valid String email) {
        return getAuthUserByEmail.execute(email);
    }

    public Optional<AuthUser> getUserByUserName(String username) {
        return getAuthUserByUserName.execute(username);
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

    public void logout(String accessToken, String refreshToken) {
        logout.execute(accessToken, refreshToken);
    }
}