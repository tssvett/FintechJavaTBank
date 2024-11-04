package org.example.task12.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.task12.entity.ApiUser;
import org.example.task12.properties.JwtProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String generateToken(ApiUser apiUser, boolean rememberMe) {

        return Jwts.builder()
                .claims(new HashMap<>())
                .subject(apiUser.getUsername())
                .issuedAt(getIssuedAt())
                .expiration(getExpiration(rememberMe))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private static Date getIssuedAt() {
        return new Date(System.currentTimeMillis());
    }

    private Date getExpiration(boolean rememberMe) {
        Duration expirationTime = rememberMe ? jwtProperties.getRememberMeExpiration() : jwtProperties.getDefaultExpiration();

        return new Date(System.currentTimeMillis() + expirationTime.toMillis());
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getJwtKey()));
    }
}