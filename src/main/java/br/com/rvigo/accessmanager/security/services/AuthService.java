package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.security.exceptions.BadCredentialsException;
import br.com.rvigo.accessmanager.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private JwtTokenService jwtTokenService;
    private BCryptPasswordEncoder encoder;

    public Jwt authenticateUser(UserDTO userDTO) {
        User storedUser = userService.findUserByUsername(userDTO.getUsername());

        //create entity
        User user = new User(userDTO);

        if (encoder.matches(user.getPassword(), storedUser.getPassword())) {
            return jwtTokenService.generateToken(storedUser);
        }

        log.error("Invalid password! " + user);
        throw new BadCredentialsException("Invalid password");
    }
}
