package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.dtos.AccessTokenDTO;
import br.com.rvigo.accessmanager.services.UserService;
import br.com.rvigo.accessmanager.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private UserService userService;
    private JwtTokenService jwtTokenService;
    private BCryptPasswordEncoder encoder;

    public AccessTokenDTO authenticateUser(UserDTO userDTO) {

        User storedUser = userService.findUserByUsername(userDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        //create entity
        User user = new User(userDTO);
        if (encoder.matches(user.getPassword(), storedUser.getPassword())) {
            return jwtTokenService.generateToken(storedUser);
        }

        throw new RuntimeException("invalid password");
    }
}
