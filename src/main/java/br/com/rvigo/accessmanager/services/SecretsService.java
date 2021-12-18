package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.entities.Secret;
import br.com.rvigo.accessmanager.repositories.SecretsRepository;
import br.com.rvigo.accessmanager.security.exceptions.NoSuchSecretException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SecretsService {
    private SecretsRepository secretsRepository;

    public Page<Secret> findAllByUserId(UUID userId, Pageable pageable) {
        return secretsRepository.findAllSecretsByUserId(userId, pageable);
    }

    public SecretDTO createSecret(SecretDTO secretDTO) {
        Secret secret = new Secret(secretDTO);
        secret.setId(UUID.randomUUID());
        secret.setSalt(UUID.randomUUID().toString());
        Secret response = secretsRepository.save(secret);
        log.info("The secret was saved");
        return new SecretDTO(response);
    }

    public void deleteSecret(UUID secretId) {
        secretsRepository.deleteById(secretId);
        log.info("The secret was deleted");
    }

    public SecretDTO updateSecret(SecretDTO secretDTO) {
        Secret incomingSecret = new Secret(secretDTO);
        Secret currentSecret = secretsRepository.findById(incomingSecret.getId())
                .orElseThrow(NoSuchSecretException::new);

        if (currentSecret.equals(incomingSecret)) {
            log.warn("No changes! Nothing to do");
            return new SecretDTO(incomingSecret);
        } else {
            Secret updated = secretsRepository.save(incomingSecret);
            return new SecretDTO(updated);
        }
    }
}
