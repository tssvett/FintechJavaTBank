package org.example.task12.security.exceptions;

public class JwtUsernameNotFoundException extends RuntimeException {
    public JwtUsernameNotFoundException(String usernameNotFound) {
        super(usernameNotFound);
    }
}
