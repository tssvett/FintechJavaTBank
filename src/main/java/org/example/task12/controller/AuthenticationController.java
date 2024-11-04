package org.example.task12.controller;

import lombok.RequiredArgsConstructor;
import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.security.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("api/v1/auth/register")
    public AuthenticationResponse register(@RequestBody RegistrationRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("api/v1/auth/authenticate")
    public AuthenticationResponse authentication(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }
}
