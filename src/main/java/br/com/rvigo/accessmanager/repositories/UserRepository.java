package br.com.rvigo.accessmanager.repositories;

import br.com.rvigo.accessmanager.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
}
