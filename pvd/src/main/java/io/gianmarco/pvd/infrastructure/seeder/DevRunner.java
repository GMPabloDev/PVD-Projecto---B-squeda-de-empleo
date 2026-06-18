package io.gianmarco.pvd.infrastructure.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevRunner implements CommandLineRunner {
    private final DatabaseCleaner databaseCleaner;

    @Override
    public void run(String... args) throws Exception {
        databaseCleaner.clean();
    }
}
