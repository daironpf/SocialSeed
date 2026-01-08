package com.socialseed.validation.rules;

public final class UsernameRules {

    private UsernameRules() {}

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 20;

    public static final String REGEX = "^[a-zA-Z0-9._-]+$";
}