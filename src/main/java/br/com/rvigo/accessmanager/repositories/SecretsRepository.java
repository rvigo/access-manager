package br.com.rvigo.accessmanager.repositories;

import br.com.rvigo.accessmanager.entities.Secret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecretsRepository extends MongoRepository<Secret, UUID> {
    Page<Secret> findAllSecretsByUserId(UUID userId, Pageable pageable);

}
