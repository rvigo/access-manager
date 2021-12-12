package br.com.rvigo.accessmanager.controllers;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.security.dtos.AccessTokenDTO;
import br.com.rvigo.accessmanager.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Auth Controller", description = "Endpoints to handle authorization")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = "application/json")
public class AuthenticationController {
    private final AuthService authService;

    @Operation(summary = "Authenticate the user", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = AccessTokenDTO.class))),
            @ApiResponse(responseCode = "401", content = @Content)
    })
    @PostMapping("/auth")
    @ResponseStatus(CREATED)
    public AccessTokenDTO auth(@Parameter(name = "User DTO", description = "The user dto with authentication info", required = true)
                                   @RequestBody UserDTO dto) {
        return authService.authenticateUser(dto);
    }
}
