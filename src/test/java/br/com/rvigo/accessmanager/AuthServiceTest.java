package br.com.rvigo.accessmanager;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.security.services.AuthService;
import br.com.rvigo.accessmanager.security.services.JwtTokenService;
import br.com.rvigo.accessmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class AuthServiceTest {
    @MockBean
    private UserService userService;
    private JwtTokenService jwtTokenService;
    @MockBean
    private BCryptPasswordEncoder encoder;
    private AuthService userAuthService;
    private UserDTO userDTO;
    private User user;
    private final UUID userId = UUID.fromString("791d37d4-535b-4f00-958f-4e0914606c6a");

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setId(userId);
        userDTO.setPassword("test");
        String key = "AVERYVERYLONGSTRINGTOMATCHNEEDEDBITSFORTHEHS512ALGORITHMACCORDINGTOJWTJWASPECIFIATION(RFC7518SECTION3.2)";

        user = new User(userDTO);

        jwtTokenService = new JwtTokenService(30000L, key);
        userAuthService = new AuthService(userService, jwtTokenService, encoder);
    }

    @Test
    public void shouldAuthenticateAnUser() {
        Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(Optional.ofNullable(user));
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Jwt jwt = userAuthService.authenticateUser(userDTO);

        assertNotNull(jwt);
        assertNotNull(jwt.getJwtToken());
    }
}
