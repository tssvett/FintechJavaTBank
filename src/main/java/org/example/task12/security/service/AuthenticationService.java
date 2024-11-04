package org.example.task12.security.service;

import lombok.RequiredArgsConstructor;
import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.entity.ApiUser;
import org.example.task12.entity.JwtToken;
import org.example.task12.enums.Role;
import org.example.task12.security.exceptions.UserAlreadyRegisteredException;
import org.example.task12.security.repository.ApiUserRepository;
import org.example.task12.security.repository.JwtRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApiUserRepository apiUserRepository;
    private final JwtRepository jwtRepository;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegistrationRequest request) {

        apiUserRepository.findByLogin(request.login())
                .ifPresent(user -> {
                    throw new UserAlreadyRegisteredException("User with login " + request.login() + " already exists");
                });

        ApiUser apiUser = ApiUser.builder()
                .login(request.login())
                .password(request.password())
                .role(Role.USER)
                .build();

        apiUserRepository.save(apiUser);

        boolean rememberMe = false;
        String jwtToken = jwtService.generateToken(apiUser, rememberMe);

        jwtRepository.save(JwtToken.builder()
                .token(jwtToken)
                .revoked(false)
                .user(apiUser)
                .build());

        return new AuthenticationResponse(jwtToken);

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
