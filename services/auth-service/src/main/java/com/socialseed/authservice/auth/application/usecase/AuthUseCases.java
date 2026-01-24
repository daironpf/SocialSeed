package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;
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
    private final RefreshToken refreshToken;
    public final ForgotPassword forgotPassword;
    public final ResetPassword resetPassword;
    private final VerifyEmail verifyEmail;
    private final ResendVerificationEmail resendVerificationEmail;
    public final GetUserRoles getUserRoles;
    public final InitiateEmailChange initiateEmailChange;
    public final VerifyEmailChange verifyEmailChange;

    public AuthUseCases(
            AuthenticateUser authenticateUser,
            RegisterUser registerUser,
            ChangeUserPassword changeUserPassword,
            GetUserById getUserById,
            GetAuthUserByEmail getAuthUserByEmail,
            GetAuthUserByUserName getAuthUserByUserName,
            Logout logout,
            RefreshToken refreshToken,
            ForgotPassword forgotPassword,
            ResetPassword resetPassword,
            VerifyEmail verifyEmail,
            ResendVerificationEmail resendVerificationEmail,
            GetUserRoles getUserRoles,
            InitiateEmailChange initiateEmailChange,
            VerifyEmailChange verifyEmailChange) {
        this.authenticateUser = authenticateUser;
        this.registerUser = registerUser;
        this.changeUserPassword = changeUserPassword;
        this.getUserById = getUserById;
        this.getAuthUserByEmail = getAuthUserByEmail;
        this.getAuthUserByUserName = getAuthUserByUserName;
        this.logout = logout;
        this.refreshToken = refreshToken;
        this.forgotPassword = forgotPassword;
        this.resetPassword = resetPassword;
        this.verifyEmail = verifyEmail;
        this.resendVerificationEmail = resendVerificationEmail;
        this.getUserRoles = getUserRoles;
        this.initiateEmailChange = initiateEmailChange;
        this.verifyEmailChange = verifyEmailChange;
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

    public AuthResult login(String email, String password, String ip) {
        return authenticateUser.execute(email, password, ip);
    }

    public AuthResult register(AuthUser authUser) {
        return registerUser.execute(authUser);
    }

    public void changeUserPassword(UUID userId, String currentPassword, String newPassword) {
        changeUserPassword.execute(userId, currentPassword, newPassword);
    }

    public void logout(String accessToken, String refreshToken) {
        logout.execute(accessToken, refreshToken);
    }

    public AuthResult refreshToken(String token) {
        return refreshToken.execute(token);
    }

    public void forgotPassword(String email) {
        forgotPassword.execute(email);
    }

    public void resetPassword(String token, String newPassword) {
        resetPassword.execute(token, newPassword);
    }

    public void verifyEmail(String token) {
        verifyEmail.execute(token);
    }

    public void resendVerificationEmail(String email) {
        resendVerificationEmail.execute(email);
    }

    public java.util.Set<String> getUserRoles(java.util.UUID userId) {
        return getUserRoles.execute(userId);
    }

    public void initiateEmailChange(java.util.UUID userId, String newEmail) {
        initiateEmailChange.execute(userId, newEmail);
    }

    public void verifyEmailChange(String token) {
        verifyEmailChange.execute(token);
    }
}