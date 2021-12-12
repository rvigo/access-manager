package br.com.rvigo.accessmanager.controllers;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import br.com.rvigo.accessmanager.services.SecretsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Secrets Controller", description = "Endpoints to handle secrets")
@SecurityRequirement(name = "bearer")
@RestController()
@RequestMapping(value = "/api/secret", produces = "application/json", headers = "correlation-id")
@AllArgsConstructor
public class SecretsController {
    private SecretsService secretsService;

    @Operation(summary = "Get all secrets by user id", responses = {
            @ApiResponse(responseCode = "200", description = "A pageable list with all user secrets"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized", content = @Content)})
    @GetMapping
    public Page<SecretDTO> getAll(@Parameter(description = "Pagination info") @PageableDefault(sort = "id", direction = ASC) Pageable pageable,
                                  @Parameter(name = "User Id", description = "The user id") @RequestParam UUID userId) {
        return secretsService.findAllByUserId(userId, pageable);
    }

    @Operation(summary = "Create new secret", responses = {
            @ApiResponse(responseCode = "201",
                    description = "The secret has been created with success"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized", content = @Content)})
    @PostMapping
    @ResponseStatus(CREATED)
    public SecretDTO createNew(@RequestBody SecretDTO secretDTO) {
        return secretsService.createSecret(secretDTO);
    }

    @Operation(summary = "Update a secret", responses = {
            @ApiResponse(responseCode = "201",
                    description = "The secret has been updated with success"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized", content = @Content)})
    @PutMapping
    @ResponseStatus(CREATED)
    public SecretDTO update(@RequestBody SecretDTO secretDTO) {
        return secretsService.updateSecret(secretDTO);
    }

    @Operation(summary = "Delete a secret", responses = {
            @ApiResponse(responseCode = "204",
                    description = "The given secret has been deleted with success", content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The given secret does not exists", content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized", content = @Content)})
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void delete(@Parameter(name = "Secret id", description = "Secret id to be deleted", required = true) @RequestParam UUID secretId) {
        secretsService.deleteSecret(secretId);
    }
}
