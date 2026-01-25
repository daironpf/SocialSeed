package com.socialseed.validation.annotation;

import com.socialseed.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Formato de nombre de usuario inválido (3-20 caracteres, alfanumérico y _)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
