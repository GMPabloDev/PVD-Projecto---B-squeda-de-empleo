package io.gianmarco.pvd.application.services;

public interface EmailService {
    void send(String to, String subject, String body);
}
