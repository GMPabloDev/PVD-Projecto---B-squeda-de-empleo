package io.gianmarco.pvd.infrastructure.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.gianmarco.pvd.application.services.HashService;

@Service
public class HashServiceImpl implements HashService {
    private final PasswordEncoder encoder;

    public HashServiceImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hash(String value) {
        return encoder.encode(value);
    }

    @Override
    public boolean compare(String value, String hash) {
        return encoder.matches(value, hash);
    }
}
