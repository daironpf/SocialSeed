package com.socialseed.validation.annotation;

import com.socialseed.validation.constraint.UUIDValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UUIDValidator.class)
public @interface ValidUUID {
    String message() default "{error.invalid.uuid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
