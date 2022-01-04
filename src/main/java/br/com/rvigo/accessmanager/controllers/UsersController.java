package br.com.rvigo.accessmanager.controllers;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
@AllArgsConstructor
public class UsersController {
    private UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO createNew(@RequestBody UserDTO userDTO) {
        return userService.createNewUser(userDTO);
    }

    @GetMapping
    public UserDTO getUserById(@RequestParam UUID id){
        return new UserDTO(userService.findUserById(id));
    }
}
