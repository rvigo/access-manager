package br.com.rvigo.accessmanager.controllers;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.services.SecretsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/api/secret", produces = "application/json")
@AllArgsConstructor
public class SecretsController {
    private SecretsService secretsService;

    @GetMapping
    public Page<SecretDTO> getAll(@PageableDefault(sort = "url", direction = ASC) Pageable pageable,
                               @RequestParam UUID userId) {
        return secretsService.findAllByUserId(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public SecretDTO createNew(@RequestBody SecretDTO secretDTO) {
        return secretsService.createSecret(secretDTO);
    }

    @PatchMapping
    public SecretDTO update(@RequestBody SecretDTO secretDTO) {
        return secretsService.updateSecret(secretDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam UUID secretId) {
        secretsService.deleteSecret(secretId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
