package io.gianmarco.pvd.application.services;

import java.util.UUID;

import io.gianmarco.pvd.domain.entities.User;

public interface TokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(UUID userId);

    UUID validateAccessToken(String token); // retorna userId o lanza excepción

    UUID validateRefreshToken(String token); // retorna userId o lanza excepción
}
