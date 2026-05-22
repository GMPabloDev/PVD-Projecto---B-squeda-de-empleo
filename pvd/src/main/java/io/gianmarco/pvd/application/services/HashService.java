package io.gianmarco.pvd.application.services;

public interface HashService {
    String hash(String value);
    boolean compare(String value, String hash);
}
