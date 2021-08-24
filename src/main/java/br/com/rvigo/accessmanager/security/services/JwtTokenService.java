package br.com.rvigo.accessmanager.security.services;

import br.com.rvigo.accessmanager.entities.User;
import br.com.rvigo.accessmanager.security.entities.Jwt;
import br.com.rvigo.accessmanager.security.exceptions.JwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Service
public class JwtTokenService {
    private final Long tokenExpiration;
    private final String secretKey;

    public JwtTokenService(@Value("${token-expiration-ms:30000}") Long tokenExpiration, @Value("${secret-key}") String secretKey) {
        this.tokenExpiration = tokenExpiration;
        this.secretKey = secretKey;
    }

    private static final SignatureAlgorithm DEFAULT_SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String AUDIENCE = "PWDMNGMT";
    private static final String ISSUER = "Access Manager";


    public UUID getUserIdFromToken(String token) {
        Claims claims = parseJWT(token);
        return UUID.fromString(claims.getSubject());
    }

    public Jwt generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (!authorities.isEmpty()) {
            claims.put("roles", authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","))
            );
        }

        String token = Jwts.builder()
                .setClaims(claims)
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(currentTimeMillis()))
                .setExpiration(new Date(currentTimeMillis() + tokenExpiration))
                .signWith(getSecretKey(), DEFAULT_SIGNATURE_ALGORITHM)
                .compact();

        return new Jwt(token, tokenExpiration);
    }

    public Boolean validateToken(String token, User user) {
        Claims claims = parseJWT(token);
        return (user.getId().equals(UUID.fromString(claims.getSubject())));
    }

    private Claims parseJWT(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (SecurityException e) {
            log.error("Invalid JWT signature");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }
        throw new JwtTokenException();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
