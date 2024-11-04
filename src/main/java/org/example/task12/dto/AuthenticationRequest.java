package org.example.task12.dto;

public record AuthenticationRequest(
        String login,
        String password,
        boolean rememberMe
) {
}
