package com.socialseed.authservice.platform.validation.annotation;

import com.socialseed.authservice.platform.validation.constraint.UsernameConstraint;
import com.socialseed.authservice.platform.validation.rules.UsernameRules;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "{username.invalid}";

    int min() default UsernameRules.MIN_LENGTH;
    int max() default UsernameRules.MAX_LENGTH;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
