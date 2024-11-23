package org.example.task12.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.ChangePasswordRequest;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.security.service.AuthenticationService;
import org.example.task15.metrics.CustomMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CustomMetricService customMetricService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);


    @PostMapping("api/v1/auth/register")
    public AuthenticationResponse register(@RequestBody RegistrationRequest request) {

        try (var ignored = MDC.putCloseable("Username", request.username())) {
            log.info("Request started");
            customMetricService.incrementRequestCount();
            AuthenticationResponse register = authenticationService.register(request);
            log.info("Request finished");
            return register;
        }
    }

    @PostMapping("api/v1/auth/authenticate")
    public AuthenticationResponse authentication(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("api/v1/auth/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader("Authorization") String token) {
        authenticationService.logout(token);
    }

    @PostMapping("api/v1/auth/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordRequest request,
                               @RequestHeader("Authorization") String token) {
        authenticationService.changePassword(request, token);
    }

    @GetMapping("api/v1/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin() {
        return "Admin endpoint";
    }
}
