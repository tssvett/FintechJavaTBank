package org.example.task12.security.exceptions;

public class JwtTokenNotFoundException extends RuntimeException {
    public JwtTokenNotFoundException(String tokenNotFound) {
        super(tokenNotFound);
    }
}
