package com.socialseed.validation.constraint;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.socialseed.validation.rules.UsernameRules;

public class UsernameConstraint
        implements ConstraintValidator<ValidUsername, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidUsername constraint) {
        this.min = constraint.min();
        this.max = constraint.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Null o vacío
        if (value == null || value.isBlank()) {
            buildViolation(context, "{username.empty}");
            return false;
        }

        // Tamaño
        int length = value.length();
        if (length < min || length > max) {
            buildViolation(context, "{username.size.invalid}");
            return false;
        }

        // Formato
        if (!value.matches(UsernameRules.REGEX)) {
            buildViolation(context, "{username.format.invalid}");
            return false;
        }
        return true;
    }
    private void buildViolation(
            ConstraintValidatorContext context,
            String message
    ) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}