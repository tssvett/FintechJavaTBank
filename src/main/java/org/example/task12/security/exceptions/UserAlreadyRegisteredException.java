package org.example.task12.security.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String s) {
        super(s);
    }
}
