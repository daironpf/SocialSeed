package com.socialseed.authservice.platform.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.socialseed.authservice.platform.validation.constraint.PasswordConstraint;
import com.socialseed.authservice.platform.validation.rules.PasswordRules;

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
