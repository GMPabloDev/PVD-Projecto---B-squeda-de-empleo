package io.gianmarco.pvd.application.services;

public interface HasherService {
    String hash(String value);
    boolean compare(String value, String hash);
}
