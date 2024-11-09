package org.example.task12.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.task12.properties.JwtProperties;
import org.example.task12.security.exceptions.JwtTokenNotFoundException;
import org.example.task12.security.repository.JwtRepository;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtExtractUtils {
    private final JwtProperties jwtProperties;
    private final JwtRepository jwtRepository;

    public Optional<String> getUserName(String token) {
        return Optional.ofNullable(getClaims(token).getSubject());
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenRevoked(String token) {
        return jwtRepository.findByToken(token)
                .orElseThrow(() -> new JwtTokenNotFoundException("Token not found"))
                .isRevoked();
    }

    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getJwtKey()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
