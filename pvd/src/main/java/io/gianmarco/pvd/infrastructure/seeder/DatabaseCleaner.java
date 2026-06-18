package io.gianmarco.pvd.infrastructure.seeder;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseCleaner {

    private final EntityManager entityManager;

    @Transactional
    public void clean() {
        // Desactivar restricciones para poder truncar en cualquier orden
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        // Lista de todas las tablas en orden lógico (o inverso de dependencias)
        String[] tables = {
                "user_roles", // Tabla de colección (generada por @ElementCollection)
                "refresh_tokens",
                "sessions",
                "otps",
                "users"
        };

        for (String table : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
        }

        // Reactivar restricciones
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}