package com.socialseed.validation.validator;

import com.socialseed.validation.annotation.ValidRole;
import com.socialseed.validation.rules.RoleRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    
    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }
        return RoleRules.isValidRole(role.trim());
    }
}