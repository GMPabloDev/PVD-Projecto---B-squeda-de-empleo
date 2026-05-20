package io.gianmarco.pvd.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import io.gianmarco.pvd.domain.entities.User;
import io.gianmarco.pvd.domain.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    
    }

    @Override
    public Optional<User> findById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public boolean existsByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByEmail'");
    }

}
