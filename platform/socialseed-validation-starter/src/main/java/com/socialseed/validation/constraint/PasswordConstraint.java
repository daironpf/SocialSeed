package com.socialseed.validation.constraint;

import com.socialseed.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.socialseed.validation.rules.PasswordRules;

public class PasswordConstraint
        implements ConstraintValidator<ValidPassword, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidPassword constraint) {
        this.min = constraint.min();
        this.max = constraint.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Null o vacío
        if (value == null || value.isBlank()) {
            buildViolation(context, "{password.empty}");
            return false;
        }

        // Tamaño
        int length = value.length();
        if (length < min || length > max) {
            buildViolation(context, "{password.size.invalid}");
            return false;
        }

        // Complejidad
        if (!value.matches(PasswordRules.REGEX)) {
            buildViolation(context, "{password.format.invalid}");
            return false;
        }

        return true;
    }

    private void buildViolation(
            ConstraintValidatorContext context,
            String messageKey
    ) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(messageKey)
                .addConstraintViolation();
    }
}
