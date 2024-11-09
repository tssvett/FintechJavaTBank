package org.example.task12.dto;

public record AuthenticationRequest(
        String username,
        String password,
        boolean rememberMe
) {
}
