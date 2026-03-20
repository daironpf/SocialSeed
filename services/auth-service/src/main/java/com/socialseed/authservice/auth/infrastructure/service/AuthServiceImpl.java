package com.socialseed.authservice.auth.infrastructure.service;

import com.socialseed.authservice.auth.config.jwt.JWTProvider;
import com.socialseed.authservice.auth.domain.event.PasswordChangedEvent;
import com.socialseed.authservice.auth.domain.event.UserRegisteredEvent;
import com.socialseed.authservice.auth.domain.repository.PasswordChangedEventPublisher;
import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.domain.model.AuthUser;
import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.repository.RefreshTokenRepository;
import com.socialseed.authservice.auth.domain.repository.UserRegisteredEventPublisher;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.authservice.auth.domain.service.TokenBlacklistService;
import com.socialseed.authservice.auth.domain.model.RefreshToken;
import com.socialseed.authservice.auth.domain.util.SecureTokenGenerator;
import com.socialseed.errorhandling.exception.BusinessException;
import com.socialseed.errorhandling.exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final UserRegisteredEventPublisher eventPublisher;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordChangedEventPublisher passwordChangedEventPublisher;
    private final com.socialseed.authservice.auth.domain.service.LoginAttemptService loginAttemptService;
    private final com.socialseed.authservice.auth.domain.service.EmailService emailService;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenDurationSeconds;

    public AuthServiceImpl(AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JWTProvider jwtProvider,
            UserRegisteredEventPublisher eventPublisher,
            RefreshTokenRepository refreshTokenRepository,
            TokenBlacklistService tokenBlacklistService,
            PasswordChangedEventPublisher passwordChangedEventPublisher,
            com.socialseed.authservice.auth.domain.service.LoginAttemptService loginAttemptService,
            com.socialseed.authservice.auth.domain.service.EmailService emailService) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.eventPublisher = eventPublisher;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.passwordChangedEventPublisher = passwordChangedEventPublisher;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @Override
    public AuthResult login(String email, String password, String ip) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // Check if account is locked
        if (!authUser.isAccountNonLocked()) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }

        // Check if credentials (password) are expired
        if (!authUser.isCredentialsNonExpired()) {
            throw new BusinessException(ErrorCode.PASSWORD_EXPIRED);
        }

        if (!passwordEncoder.matches(password, authUser.getPassword())) {
            // Record failed login in separate transaction
            loginAttemptService.recordFailedLogin(authUser.getId(), ip);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Record successful login
        loginAttemptService.recordSuccessfulLogin(authUser.getId(), ip);

        String token = jwtProvider.generateToken(authUser.getUsername());
        RefreshToken refreshToken = RefreshToken.create(authUser.getId(), refreshTokenDurationSeconds);
        refreshTokenRepository.save(refreshToken);

        Set<String> roles = authUser.getRoles();

        return new AuthResult(token, refreshToken.getToken(), roles);
    }

    @Transactional
    @Override
    public AuthResult register(AuthUser authUser, UUID id) {

        AuthUser newAuthUser = new AuthUser(
                id,
                authUser.getUsername(),
                authUser.getEmail(),
                passwordEncoder.encode(authUser.getPassword()));

        // Generate verification token (24 hours expiry)
        String verificationToken = SecureTokenGenerator.generate();
        java.time.Instant expiry = java.time.Instant.now().plusSeconds(86400);
        newAuthUser.setVerificationToken(verificationToken);
        newAuthUser.setVerificationTokenExpiry(expiry);
        newAuthUser.setEmailVerified(false);
        newAuthUser.setLastPasswordChangedAt(java.time.Instant.now());

        authUserRepository.save(newAuthUser);

        // Send verification email
        emailService.sendVerificationEmail(authUser.getEmail(), verificationToken);

        // Emit to Kafka Server
        UserRegisteredEvent event = new UserRegisteredEvent(
                id,
                authUser.getEmail(),
                authUser.getEmail(),
                System.currentTimeMillis());
        // eventPublisher.publish(event); // Kafka desactivado para pruebas locales sin
        // broker

        // Generate Tokens
        String token = jwtProvider.generateToken(newAuthUser.getUsername());
        RefreshToken refreshToken = RefreshToken.create(id, refreshTokenDurationSeconds);
        refreshTokenRepository.save(refreshToken);

        Set<String> roles = newAuthUser.getRoles();

        return new AuthResult(token, refreshToken.getToken(), roles);
    }

    @Override
    public AuthUser createUser(AuthUser authUser) {
        return null;
    }

    // region Gets
    @Override
    public Optional<AuthUser> getUserById(UUID id) {
        return authUserRepository.findById(id);
    }



    @Override
    public Optional<AuthUser> getUserByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public Optional<AuthUser> getUserByUserName(String username) {
        return authUserRepository.findByUserName(username);
    }
    // endregion

    // region Exists
    @Override
    public boolean existByUserId(UUID id) {
        return authUserRepository.existByUserId(id);
    }
    // endregion

    @Override
    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        AuthUser authUser = authUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, authUser.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // Update password
        authUser.setPassword(passwordEncoder.encode(newPassword));
        authUser.setLastPasswordChangedAt(java.time.Instant.now());
        authUser.setCredentialsNonExpired(true);
        authUserRepository.save(authUser);

        // Invalidate all refresh tokens for the user
        refreshTokenRepository.deleteByUserId(userId);

        // Emit PasswordChangedEvent
        PasswordChangedEvent event = new PasswordChangedEvent(
                userId,
                authUser.getEmail(),
                System.currentTimeMillis()
        );
        passwordChangedEventPublisher.publish(event);
    }

    @Override
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        // 1. Invalidate Refresh Token
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
                token.revoke();
                refreshTokenRepository.save(token);
            });
        }

        // 2. Blacklist Access Token
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            String jti = jwtProvider.getJtiFromToken(token);
            Date expiry = jwtProvider.getExpirationDateFromToken(token);

            long ttlSeconds = (expiry.getTime() - System.currentTimeMillis()) / 1000;
            if (ttlSeconds > 0) {
                tokenBlacklistService.blacklistToken(jti, Duration.ofSeconds(ttlSeconds));
            }
        }
    }

    @Override
    @Transactional
    public AuthResult refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshToken.isRotated()) {
            // REUSE DETECTION: If a rotated token is used again, it's a security breach.
            // Revoke all tokens for this user.
            refreshTokenRepository.deleteByUserId(refreshToken.getUserId());
            throw new BusinessException(ErrorCode.AUTH_REUSE_DETECTION);
        }

        if (!refreshToken.isValid()) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID_EXPIRED);
        }

        // 1. Mark old token as rotated
        refreshToken.rotate();
        refreshTokenRepository.save(refreshToken);

        // 2. Get User
        AuthUser authUser = authUserRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 3. Generate new tokens
        String newAccessToken = jwtProvider.generateToken(authUser.getUsername());
        RefreshToken newRefreshToken = RefreshToken.create(authUser.getId(), refreshTokenDurationSeconds);
        refreshTokenRepository.save(newRefreshToken);


        return new AuthResult(newAccessToken, newRefreshToken.getToken(), authUser.getRoles());
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        AuthUser authUser = authUserRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_TOKEN_INVALID));

        // Check if already verified
        if (authUser.isEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // Check if token is expired
        if (authUser.getVerificationTokenExpiry() == null ||
                java.time.Instant.now().isAfter(authUser.getVerificationTokenExpiry())) {
            throw new BusinessException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
        }

        // Mark email as verified and clear token (single-use)
        authUser.setEmailVerified(true);
        authUser.setVerificationToken(null);
        authUser.setVerificationTokenExpiry(null);
        authUserRepository.save(authUser);
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_EMAIL_NOT_FOUND));

        // Check if already verified
        if (authUser.isEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        // Generate new verification token (24 hours expiry)
        String verificationToken = SecureTokenGenerator.generate();
        java.time.Instant expiry = java.time.Instant.now().plusSeconds(86400);
        authUser.setVerificationToken(verificationToken);
        authUser.setVerificationTokenExpiry(expiry);
        authUserRepository.save(authUser);

        // Send verification email
        emailService.sendVerificationEmail(email, verificationToken);
    }

    @Override
    @Transactional
    public void saveUser(AuthUser user) {
        authUserRepository.save(user);
    }

    @Override
    public void revokeAllTokensForUser(UUID userId) {
        for (RefreshToken token : refreshTokenRepository.findByUserId(userId)) {
            token.revoke();
            refreshTokenRepository.save(token);
        }
    }

    @Override
    public long countUsersWithRole(String role) {
        return authUserRepository.countUsersWithRole(role);
    }
}
