package org.example.task12.security.exceptions;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String tokenExpired) {
        super(tokenExpired);
    }
}
