package com.socialseed.validation.rules;

public final class PasswordRules {

    private PasswordRules() {}

    /**
     * Minimum number of characters allowed for a password.
     */
    public static final int MIN_LENGTH = 8;

    /**
     * Maximum number of characters allowed for a password.
     */
    public static final int MAX_LENGTH = 100;

    /**
     * Regular expression for password validation.
     * <p>
     * Requirements:
     * <ul>
     * <li>{@code (?=.*[a-z])} : Must contain at least one lowercase letter.</li>
     * <li>{@code (?=.*[A-Z])} : Must contain at least one uppercase letter.</li>
     * <li>{@code (?=.*\\d)} : Must contain at least one digit (0-9).</li>
     * <li>{@code (?=.*[@$!%*?&.#_-])} : Must contain at least one special character from: {@code @ $ ! % * ? & . # _ -}</li>
     * <li>{@code [A-Za-z\\d@$!%*?&.#_-]+} : Only these characters are allowed throughout the string.</li>
     * </ul>
     * Note: This regex does not enforce length. Length should be validated separately
     * using {@link #MIN_LENGTH} and {@link #MAX_LENGTH}.
     */
    public static final String REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]+$";
}