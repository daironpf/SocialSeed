package com.socialseed.validation.annotation;

import com.socialseed.validation.constraint.PasswordConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.socialseed.validation.rules.PasswordRules;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "{password.invalid}";

    int min() default PasswordRules.MIN_LENGTH;
    int max() default PasswordRules.MAX_LENGTH;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
