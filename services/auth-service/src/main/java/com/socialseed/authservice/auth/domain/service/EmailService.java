package com.socialseed.authservice.auth.domain.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
