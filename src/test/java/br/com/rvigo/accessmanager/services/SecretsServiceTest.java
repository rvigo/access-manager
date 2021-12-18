package br.com.rvigo.accessmanager.services;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.entities.Secret;
import br.com.rvigo.accessmanager.security.exceptions.NoSuchSecretException;
import br.com.rvigo.accessmanager.repositories.SecretsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class SecretsServiceTest {

    private SecretsService service;

    @MockBean
    private SecretsRepository secretsRepository;

    private Pageable pageable;
    private SecretDTO secretDTO;
    private Secret secret;

    private static final UUID USER_ID = randomUUID();

    @BeforeEach
    public void setUp() {
        this.secretDTO = new SecretDTO();
        this.secretDTO.setSalt(randomUUID().toString());
        this.secretDTO.setId(randomUUID());
        this.secretDTO.setUserId(USER_ID);
        this.secretDTO.setUsername("testUsername");
        this.secretDTO.setPassword("testPassword");
        this.secret = new Secret(secretDTO);
        this.pageable = Pageable.ofSize(1);
        this.service = new SecretsService(secretsRepository);

    }

    @Test
    public void shouldCreateAList() {
        when(secretsRepository.save(any(Secret.class)))
                .thenReturn(new Secret(secretDTO));

        SecretDTO result = service.createSecret(secretDTO);

        assertEquals(new Secret(secretDTO), new Secret(result));
    }

    @Test
    public void shouldDeleteASecret() {
        doNothing().when(secretsRepository).deleteById(any(UUID.class));

        service.deleteSecret(secretDTO.getUserId());

        verify(secretsRepository, times(1)).deleteById(secretDTO.getUserId());
    }

    @Test
    public void shouldReturnAListOfSecrets() {
        when(secretsRepository.findAllSecretsByUserId(any(UUID.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Secret())));

        Page<Secret> result = service.findAllByUserId(USER_ID, pageable);

        assertEquals(1, result.getTotalPages());
    }

    @Test
    public void shouldUpdateASecret() {
        secret.setPassword("newTestPassword");
        when(secretsRepository.findById(any(UUID.class))).thenReturn(Optional.of(secret));
        when(secretsRepository.save(any(Secret.class))).thenReturn(new Secret(secretDTO));

        SecretDTO result = service.updateSecret(this.secretDTO);

        assertEquals(new Secret(secretDTO), new Secret(result));
        verify(secretsRepository, times(1)).findById(any(UUID.class));
        verify(secretsRepository, times(1)).save(any(Secret.class));
    }

    @Test
    public void shouldReturnTheSameObjectIfNoChangesWasMadeWhileUpdating() {
        when(secretsRepository.save(any(Secret.class))).thenReturn(new Secret(secretDTO));
        when(secretsRepository.findById(any(UUID.class))).thenReturn(Optional.of(secret));

        SecretDTO result = service.updateSecret(this.secretDTO);

        assertEquals(new Secret(secretDTO), new Secret(result));
        verify(secretsRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void shouldThrowAnExceptionWhenSecretNotFoundWhileUpdating() {
        when(secretsRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchSecretException.class, () -> service.updateSecret(this.secretDTO));

        verify(secretsRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void shouldReturnTheSameSecretWhenNoNewInformationIsGiven() {
        when(secretsRepository.findById(any(UUID.class))).thenReturn(Optional.of(secret));

        SecretDTO result = service.updateSecret(this.secretDTO);

        assertEquals(new Secret(secretDTO), new Secret(result));
        verify(secretsRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(secretsRepository);
    }
}
