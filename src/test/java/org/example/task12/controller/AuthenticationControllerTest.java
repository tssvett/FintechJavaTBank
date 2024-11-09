package org.example.task12.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.App;
import org.example.task12.dto.AuthenticationRequest;
import org.example.task12.dto.AuthenticationResponse;
import org.example.task12.dto.ChangePasswordRequest;
import org.example.task12.dto.RegistrationRequest;
import org.example.task12.security.exceptions.UserAlreadyRegisteredException;
import org.example.task12.security.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link AuthenticationController}
 */

@AutoConfigureMockMvc
@SpringBootTest(classes = App.class)
@ActiveProfiles(profiles = "test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    private RegistrationRequest registrationRequest;

    private AuthenticationRequest authenticationRequest;

    private AuthenticationResponse response;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("security.jwt.jwt-key", () -> "eyJhbGciOiJIUzI1NiJ9eyJzdWIiOiJ0c3N2ZXR0c2YiLCJpYXQiOjE" +
                "3MzA4NzUxMzUsImV4cCI6MTczMDg3NTczNX0pJ3DENx6ND41ARzjvug8rYiKbW4clEipF1aqaNJfq4");
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registrationRequest = new RegistrationRequest("testUser", "testPassword");
        authenticationRequest = new AuthenticationRequest("testUser", "testPassword", true);
        response = new AuthenticationResponse("mockJwtToken");
    }

    @Test
    void testRegister_correctRequest_shouldReturnJwtToken() throws Exception {
        //Arrange
        when(authenticationService.register(registrationRequest)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(response.token()));

        verify(authenticationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_AlreadyExistingUser_shouldReturnError() throws Exception {
        //Arrange
        when(authenticationService.register(registrationRequest)).thenThrow(new UserAlreadyRegisteredException("User already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("User already exists"));

        verify(authenticationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testAuthenticate_correctRequest_shouldReturnJwtToken() throws Exception {
        //Arrange
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    void testAuthenticate_userNotFound_shouldReturnError() throws Exception {
        //Arrange
        when(authenticationService.authenticate(authenticationRequest)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User not found"));

        verify(authenticationService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    void testLogout_notValidToken_shouldForbiddenStatus() throws Exception {
        //Arrange
        doNothing().when(authenticationService).logout("errorToken");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "errorToken"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void testChangePassword_notValidToken_shouldForbiddenStatus() throws Exception {
        //Arrange
        doNothing().when(authenticationService).changePassword(any(ChangePasswordRequest.class), any(String.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "errorToken"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}
