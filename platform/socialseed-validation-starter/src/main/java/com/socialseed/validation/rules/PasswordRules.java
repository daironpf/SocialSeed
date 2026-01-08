package com.socialseed.validation.rules;

public final class PasswordRules {

    private PasswordRules() {}

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 100;

    public static final String REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]+$";
}