package com.game.backend.security.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
