package br.com.fiap.agrosat.security;

import br.com.fiap.agrosat.model.entity.Usuario;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    private final Long expiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .claim("role", usuario.getRole().name())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Instant getExpiration(String token) {

        return getClaims(token)
                .getExpiration()
                .toInstant();
    }

    public Long getUserId(String token){

        Claims claims = getClaims(token);

        return ((Number) claims.get("userId"))
                .longValue();
    }
}