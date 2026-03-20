package com.socialseed.validation.annotation;

import com.socialseed.validation.validator.RoleValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "{auth.error.invalid_role}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}