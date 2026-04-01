package com.socialseed.authservice.auth.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResultTest {

    @Test
    void shouldCreateAuthResultWithAllFields() {
        String token = "jwt-token-123";
        String refreshToken = "refresh-token-456";
        Set<String> roles = Set.of("ROLE_USER");

        AuthResult result = new AuthResult(token, refreshToken, roles);

        assertThat(result.token()).isEqualTo(token);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
        assertThat(result.roles()).containsExactly("ROLE_USER");
    }

    @Test
    void shouldCreateAuthResultWithMultipleRoles() {
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");

        AuthResult result = new AuthResult("token", "refresh", roles);

        assertThat(result.roles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void recordShouldBeImmutable() {
        Set<String> roles = Set.of("ROLE_USER");
        AuthResult result = new AuthResult("token", "refresh", roles);

        assertThat(result.token()).isEqualTo("token");
        assertThat(result.refreshToken()).isEqualTo("refresh");
    }
}
