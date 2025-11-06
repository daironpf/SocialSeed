package com.socialseed.socialuserservice.platform.error;

public class UserWithIdNotFoundException extends RuntimeException {
    public UserWithIdNotFoundException(String message) {
        super(message);
    }
}
