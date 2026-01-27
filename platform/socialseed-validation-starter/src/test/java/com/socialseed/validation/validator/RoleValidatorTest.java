package com.socialseed.validation.validator;

import com.socialseed.validation.annotation.ValidRole;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private RoleValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RoleValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_ForValidRoles() {
        assertTrue(validator.isValid("ROLE_USER", context));
        assertTrue(validator.isValid("ROLE_ADMIN", context));
        assertTrue(validator.isValid("ROLE_MODERATOR", context));
        assertTrue(validator.isValid("ROLE_SUPPORT", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForInvalidRoles() {
        assertFalse(validator.isValid("INVALID_ROLE", context));
        assertFalse(validator.isValid("role_admin", context));
        assertFalse(validator.isValid("ADMIN", context));
        assertFalse(validator.isValid("USER", context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForNullRole() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_ShouldReturnFalse_ForEmptyRole() {
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }

    @Test
    void isValid_ShouldReturnTrue_ForValidRoleWithWhitespace() {
        assertTrue(validator.isValid("  ROLE_ADMIN  ", context));
    }
}