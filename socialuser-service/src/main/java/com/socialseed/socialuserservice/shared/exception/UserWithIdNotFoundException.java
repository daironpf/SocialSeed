package com.socialseed.socialuserservice.shared.exception;

public class UserWithIdNotFoundException extends RuntimeException {
    public UserWithIdNotFoundException(String message) {
        super(message);
    }
}
