package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.entities.Secret;
import br.com.rvigo.accessmanager.repositories.SecretsRepository;
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
        Page<Secret> secrets = secretsRepository.findAllSecretsByUserId(userId, pageable);
        return new PageImpl<>(secrets.stream().map(SecretDTO::new).collect(Collectors.toList()));
    }

    public SecretDTO createSecret(SecretDTO secretDTO) {
        Secret secret = new Secret(secretDTO);
        secret.setId(UUID.randomUUID());
        secret.setSalt(UUID.randomUUID().toString());
        Secret response = secretsRepository.save(secret);
        return new SecretDTO(response);
    }

    public void deleteSecret(UUID secretId) {
        secretsRepository.deleteById(secretId);
    }

    public SecretDTO updateSecret(SecretDTO secretDTO) {
        Secret secret = new Secret(secretDTO);
        Secret response = secretsRepository.save(secret);
        return new SecretDTO(response);
    }
}
