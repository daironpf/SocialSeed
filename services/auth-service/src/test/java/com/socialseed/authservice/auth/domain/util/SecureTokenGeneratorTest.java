package com.socialseed.authservice.auth.domain.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecureTokenGeneratorTest {

    @Test
    void generate_shouldReturnNonEmptyToken() {
        String token = SecureTokenGenerator.generate();

        assertThat(token).isNotBlank();
    }

    @Test
    void generate_shouldReturnUniqueTokens() {
        String token1 = SecureTokenGenerator.generate();
        String token2 = SecureTokenGenerator.generate();

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void generate_shouldReturnUrlSafeToken() {
        String token = SecureTokenGenerator.generate();

        assertThat(token).doesNotContain("+");
        assertThat(token).doesNotContain("/");
        assertThat(token).doesNotContain("=");
    }

    @Test
    void generate_shouldReturnTokenOfCorrectLength() {
        String token = SecureTokenGenerator.generate();

        assertThat(token).hasSize(43);
    }

    @Test
    void generate_shouldReturnDifferentTokensOnMultipleCalls() {
        for (int i = 0; i < 100; i++) {
            String token1 = SecureTokenGenerator.generate();
            String token2 = SecureTokenGenerator.generate();
            assertThat(token1).isNotEqualTo(token2);
        }
    }

    @Test
    void constructor_shouldBePrivate() throws NoSuchMethodException {
        assertThat(SecureTokenGenerator.class.getDeclaredConstructors())
                .hasSize(1);
        assertThat(SecureTokenGenerator.class.getDeclaredConstructors()[0].isAccessible())
                .isFalse();
    }
}
