package com.socialseed.validation.validator;

import com.socialseed.validation.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

  // 3-20 chars, alfanumeric + underscore, no spaces
  private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
  private static final Pattern PATTERN = Pattern.compile(USERNAME_PATTERN);

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    if (username == null || username.isBlank()) {
      return false; // @NotBlank should be used alongside if nullability matters, but here we
                    // enforce non-null
    }
    return PATTERN.matcher(username).matches();
  }
}
