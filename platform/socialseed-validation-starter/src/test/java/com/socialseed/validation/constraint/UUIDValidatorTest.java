package com.socialseed.validation.constraint;

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
class UUIDValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private UUIDValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UUIDValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_ForValidUUIDs() {
        assertTrue(validator.isValid("550e8400-e29b-41d4-a716-446655440000", context));
        assertTrue(validator.isValid("f47ac10b-58cc-4372-a567-0e02b2c3d479", context));
        assertTrue(validator.isValid("00000000-0000-0000-0000-000000000000", context));
        assertTrue(validator.isValid("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "550e8400-e29b-41d4-a716-44665544000",
        "550e8400e29b-41d4-a716-446655440000",
        "550e8400-e29b-41d4-a716-44665544000g",
        "550e8400-e29b-41d4a716-446655440000",
        "not-a-uuid",
        "12345"
    })
    void isValid_ShouldReturnFalse_ForInvalidUUIDs(String value) {
        assertFalse(validator.isValid(value, context));
    }

    @Test
    void isValid_ShouldReturnTrue_ForNullValue() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnTrue_ForBlankValue() {
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }
}
