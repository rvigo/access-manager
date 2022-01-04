package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.entities.Secret;
import br.com.rvigo.accessmanager.repositories.SecretsRepository;
import br.com.rvigo.accessmanager.security.exceptions.NoSuchSecretException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SecretsService {
    private SecretsRepository secretsRepository;

    public Page<SecretDTO> findAllByUserId(UUID userId, Pageable pageable) {
        return new PageImpl<SecretDTO>(secretsRepository.findAllSecretsByUserId(userId, pageable).stream().map(SecretDTO::new).collect(Collectors.toList()));
    }

    public SecretDTO createSecret(SecretDTO secretDTO) {
        Secret secret = new Secret(secretDTO);
        secret.setId(UUID.randomUUID());
        Secret response = secretsRepository.save(secret);
        log.info(String.format("The secret %s was saved", secret));
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

            if(incomingSecret.canUpdate(currentSecret)) {
                currentSecret.update(incomingSecret);
                Secret updated = secretsRepository.save(currentSecret);
                log.info(String.format("The secret was updated to %s", updated));
                return new SecretDTO(updated);
            }
            log.info("The incoming secret has no changes. Nothing to do");
            return secretDTO;
        }
}
