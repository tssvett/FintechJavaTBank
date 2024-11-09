package org.example.task12.security.service;

import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.security.exceptions.UserAlreadyRegisteredException;
import org.example.task12.security.repository.JwtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtRepository jwtRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegistrationRequest registrationRequest;

    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registrationRequest = new RegistrationRequest("testUser", "testPassword");
        authenticationRequest = new AuthenticationRequest("testUser", "testPassword", true);
    }

    @Test
    void testRegister_Success() {
        // Arrange
        when(passwordEncoder.encode(registrationRequest.password())).thenReturn("encodedPassword");

        String jwtToken = "generatedJwtToken";
        when(jwtService.generateToken(any(), eq(false))).thenReturn(jwtToken);

        // Act
        AuthenticationResponse response = authService.register(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(jwtToken, response.token());

        verify(userService).ifUserRegisteredThrowException(registrationRequest.username());
        verify(passwordEncoder).encode(registrationRequest.password());
        verify(jwtService).generateToken(any(), eq(false));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        doThrow(new UserAlreadyRegisteredException("User already exists"))
                .when(userService).ifUserRegisteredThrowException(registrationRequest.username());

        // Act & Assert
        Exception exception = assertThrows(UserAlreadyRegisteredException.class, () -> {
            authService.register(registrationRequest);
        });

        assertEquals("User already exists", exception.getMessage());

        verify(userService).ifUserRegisteredThrowException(registrationRequest.username());
        verifyNoMoreInteractions(passwordEncoder, userService, jwtService, jwtRepository);
    }
}
