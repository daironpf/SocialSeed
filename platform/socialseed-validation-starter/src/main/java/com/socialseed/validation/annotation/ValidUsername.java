package com.socialseed.validation.annotation;

import com.socialseed.validation.constraint.UsernameConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.socialseed.validation.rules.UsernameRules;

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
