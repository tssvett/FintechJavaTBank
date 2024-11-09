package org.example.task12.security.exceptions;

public class JwtTokenRevokedException extends RuntimeException {
    public JwtTokenRevokedException(String tokenRevoked) {
        super(tokenRevoked);
    }
}
