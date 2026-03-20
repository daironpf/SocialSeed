package com.socialseed.validation.validator;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsernameValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private UsernameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UsernameValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_ForValidUsernames() {
        assertTrue(validator.isValid("john", context));
        assertTrue(validator.isValid("user123", context));
        assertTrue(validator.isValid("john_doe", context));
        assertTrue(validator.isValid("User_Name_123", context));
        assertTrue(validator.isValid("abc", context));
        assertTrue(validator.isValid("abcdefghijklmnopqrst", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForTooShortUsernames() {
        assertFalse(validator.isValid("ab", context));
        assertFalse(validator.isValid("a", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForTooLongUsernames() {
        assertFalse(validator.isValid("abcdefghijklmnopqrstu", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"john.doe", "john-doe", "john doe", "user@name", "user#123"})
    void isValid_ShouldReturnFalse_ForInvalidCharacters(String username) {
        assertFalse(validator.isValid(username, context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForNullUsername() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForBlankUsername() {
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }
}
