package org.example.task12.security.service;

import lombok.RequiredArgsConstructor;
import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.ChangePasswordRequest;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.entity.ApiUser;
import org.example.task12.entity.JwtToken;
import org.example.task12.enums.Role;
import org.example.task12.security.exceptions.JwtTokenNotFoundException;
import org.example.task12.security.repository.JwtRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.task12.security.filter.JwtAuthenticationFilter.BEARER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest request) {

        userService.ifUserRegisteredThrowException(request.username());

        ApiUser apiUser = ApiUser.builder()
                .login(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userService.save(apiUser);

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

        ApiUser apiUser = userService.loadUserByUsername(request.username());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        revokeExistingTokens(apiUser);

        String jwtToken = jwtService.generateToken(apiUser, request.rememberMe());

        jwtRepository.save(JwtToken.builder()
                .token(jwtToken)
                .revoked(false)
                .user(apiUser)
                .build());

        return new AuthenticationResponse(jwtToken);
    }

    public void logout(String jwtToken) {
        jwtToken = jwtToken.replace(BEARER, "").trim();
        JwtToken token = jwtRepository.findByToken(jwtToken).orElseThrow(() -> new JwtTokenNotFoundException("Token not found"));
        ApiUser user = token.getUser();
        revokeExistingTokens(user);
    }

    public void changePassword(ChangePasswordRequest request, String jwtToken) {
        jwtToken = jwtToken.replace(BEARER, "").trim();
        JwtToken token = jwtRepository.findByToken(jwtToken).orElseThrow(() -> new JwtTokenNotFoundException("Token not found"));
        ApiUser user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userService.save(user);
    }

    private void revokeExistingTokens(ApiUser apiUser) {
        List<JwtToken> tokens = jwtRepository.findAllByUserAndRevoked(apiUser, false);
        tokens.forEach(token -> token.setRevoked(true));
        jwtRepository.saveAll(tokens);
    }
}
