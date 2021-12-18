package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.security.exceptions.BadCredentialsException;
import br.com.rvigo.accessmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AuthServiceTest {
    @MockBean
    private UserService userService;
    @MockBean
    private BCryptPasswordEncoder encoder;

    private AuthService userAuthService;
    private UserDTO userDTO;
    private User user;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setId(userId);
        userDTO.setPassword("test");
        String key = "AVERYVERYLONGSTRINGTOMATCHNEEDEDBITSFORTHEHS512ALGORITHMACCORDINGTOJWTJWASPECIFIATION(RFC7518SECTION3.2)";

        user = new User(userDTO);

        JwtTokenService jwtTokenService = new JwtTokenService(30000L, key);
        userAuthService = new AuthService(userService, jwtTokenService, encoder);
    }

    @Test
    public void shouldAuthenticateAnUser() {
        when(userService.findUserByUsername(Mockito.anyString())).thenReturn(user);
        when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Jwt jwt = userAuthService.authenticateUser(userDTO);

        assertNotNull(jwt);
        assertNotNull(jwt.getJwtToken());
    }

    @Test
    public void shouldThrowAnExceptionWhenPasswordDoesNotMatches() {
        when(userService.findUserByUsername(Mockito.anyString())).thenReturn(user);
        when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> userAuthService.authenticateUser(userDTO));

        assertEquals("Invalid password", exception.getMessage());
    }
}
