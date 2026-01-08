package com.socialseed.authservice.auth.domain.service;

public interface PasswordService {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
