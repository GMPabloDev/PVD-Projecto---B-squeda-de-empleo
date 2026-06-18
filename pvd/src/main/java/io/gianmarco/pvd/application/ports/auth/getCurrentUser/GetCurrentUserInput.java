package io.gianmarco.pvd.application.ports.auth.getCurrentUser;

import java.util.UUID;

public record GetCurrentUserInput(UUID userId) {
}
