package br.com.rvigo.accessmanager.controllers;

import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.security.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/auth")
    public Jwt auth(@RequestBody UserDTO dto) {
        return authService.authenticateUser(dto);
    }
}
