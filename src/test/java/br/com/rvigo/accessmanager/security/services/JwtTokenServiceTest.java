package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class JwtTokenServiceTest {

    private JwtTokenService service;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        String key = "AVERYVERYLONGSTRINGTOMATCHNEEDEDBITSFORTHEHS512ALGORITHMACCORDINGTOJWTJWASPECIFIATION(RFC7518SECTION3.2)";
        service = new JwtTokenService(3000L, key);
    }

    @Test
    public void shouldGetUserFromTokenClaims() {
        Jwt jwt = service.generateToken(user);

        UUID userIdFromToken = service.getUserIdFromToken(jwt.getJwtToken());

        assertEquals(user.getId(), userIdFromToken);
    }

    @Test
    public void shouldCreateAToken() {
        Jwt jwt = service.generateToken(user);

        assertEquals("Bearer", jwt.getTokenType());
        assertEquals(3000L, jwt.getDuration());
        assertNotNull(jwt.getJwtToken());
    }

    @Test
    public void shouldValidateTheToken() {
        Jwt jwt = service.generateToken(user);

        Boolean validatedToken = service.validateToken(jwt.getJwtToken(), user);

        assertTrue(validatedToken);
    }

    @Test
    public void shouldNotValidateTheTokenWithDifferentUser() {
        User newUser = new User();
        newUser.setId(UUID.randomUUID());

        Jwt jwt = service.generateToken(newUser);

        Boolean notValidatedToken = service.validateToken(jwt.getJwtToken(), user);

        assertFalse(notValidatedToken);
    }
}
