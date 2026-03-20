package com.socialseed.validation.constraint;

import com.socialseed.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordConstraintTest {

    @Mock
    private ConstraintValidatorContext context;

    private PasswordConstraint validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordConstraint();
    }

    private void initValidator(int min, int max) {
        ValidPassword mockAnnotation = mock(ValidPassword.class);
        when(mockAnnotation.min()).thenReturn(min);
        when(mockAnnotation.max()).thenReturn(max);
        validator.initialize(mockAnnotation);
    }

    @Test
    void isValid_ShouldReturnTrue_ForValidPasswords() {
        initValidator(8, 100);
        assertTrue(validator.isValid("Password1!", context));
        assertTrue(validator.isValid("Abcdefg1@", context));
        assertTrue(validator.isValid("Str0ng$P@ssword", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForTooShortPassword() {
        initValidator(8, 100);
        stubContextBuilder();
        assertFalse(validator.isValid("Pass1!", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForTooLongPassword() {
        initValidator(8, 100);
        stubContextBuilder();
        String longPassword = "A".repeat(101) + "1!";
        assertFalse(validator.isValid(longPassword, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "PASSWORD", "Password1", "Pass1234", "P@ssword"})
    void isValid_ShouldReturnFalse_ForMissingRequirements(String password) {
        initValidator(8, 100);
        stubContextBuilder();
        assertFalse(validator.isValid(password, context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForNullPassword() {
        initValidator(8, 100);
        stubContextBuilder();
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForBlankPassword() {
        initValidator(8, 100);
        stubContextBuilder();
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }

    private void stubContextBuilder() {
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }
}
