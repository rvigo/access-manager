package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.security.exceptions.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class JwtTokenServiceTest {

    private JwtTokenService service;

    private User user;
    private String key;
    private long tokenExpiration;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        key = "AVERYVERYLONGSTRINGTOMATCHNEEDEDBITSFORTHEHS512ALGORITHMACCORDINGTOJWTJWASPECIFIATION(RFC7518SECTION3.2)";
        tokenExpiration = 3000L;
        service = new JwtTokenService(tokenExpiration, key);
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
        assertEquals(tokenExpiration, jwt.getDuration());
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

    @Test
    public void shouldCreateATokenWhenUserHasGrantedAuthorities() {
        user.setAuthorities(List.of(new SimpleGrantedAuthority("TEST_AUTHORITY")));
        Jwt jwt = service.generateToken(user);

        assertEquals("Bearer", jwt.getTokenType());
        assertEquals(tokenExpiration, jwt.getDuration());
        assertNotNull(jwt.getJwtToken());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(jwt.getJwtToken())
                .getBody();

        assertTrue(claims.containsKey("roles"));
        assertEquals("TEST_AUTHORITY", claims.get("roles"));
    }

    @Test
    public void shouldThrowJwtTokenExceptionWhenParsingToken() {
        assertThrows(JwtTokenException.class, () -> service.validateToken("invalid_token", user)); //MalformedJwtException
        assertThrows(JwtTokenException.class, () -> service.validateToken("", user)); //IllegalArgumentException


        String expiredToken = Jwts.builder()
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis()))
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();

        assertThrows(JwtTokenException.class, () -> service.validateToken(expiredToken, user)); //ExpiredJwtException

        String unsupportedToken = Jwts.builder()
                .compact();

        assertThrows(JwtTokenException.class, () -> service.validateToken(unsupportedToken, user)); //UnsupportedJwtException
    }
}
