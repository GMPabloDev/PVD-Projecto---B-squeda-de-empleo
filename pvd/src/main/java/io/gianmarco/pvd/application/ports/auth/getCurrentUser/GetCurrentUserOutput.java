package io.gianmarco.pvd.application.ports.auth.getCurrentUser;

import java.util.Set;
import java.util.UUID;

public record GetCurrentUserOutput(
    UUID id,
    String name,
    String email,
    Set<String> roles,
    boolean emailVerified
) {
}
