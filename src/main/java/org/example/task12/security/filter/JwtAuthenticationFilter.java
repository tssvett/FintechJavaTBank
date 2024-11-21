package org.example.task12.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task12.entity.ApiUser;
import org.example.task12.security.exceptions.JwtTokenExpiredException;
import org.example.task12.security.exceptions.JwtTokenRevokedException;
import org.example.task12.security.exceptions.JwtUsernameNotFoundException;
import org.example.task12.security.service.UserService;
import org.example.task12.security.utils.JwtExtractUtils;
import org.example.task8.advice.details.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER = "Bearer ";
    private final UserService userService;
    private final JwtExtractUtils jwtExtractUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (isContainsJwtToken(authorizationHeader)) {

                String jwtToken = authorizationHeader.substring(BEARER.length()).trim();

                String username = jwtExtractUtils.getUserName(jwtToken)
                        .orElseThrow(() -> new JwtUsernameNotFoundException("Username not found"));

                if (authenticationIsNotPresent()) {
                    validateJwtToken(jwtToken);
                    setAuthentication(request, username);
                }
            }
            filterChain.doFilter(request, response);

        } catch (JwtUsernameNotFoundException | JwtTokenExpiredException | JwtTokenRevokedException e) {
            addExceptionDetailsToResponse(response, e.getMessage());
        }
    }

    private void addExceptionDetailsToResponse(HttpServletResponse response, String message) throws IOException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ExceptionDetails exceptionDetails = new ExceptionDetails(httpStatus.value(), message);

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionDetails));
    }

    private void validateJwtToken(String jwtToken) {
        if (jwtExtractUtils.isTokenExpired(jwtToken)) {
            throw new JwtTokenExpiredException("Token expired");
        }
        if (jwtExtractUtils.isTokenRevoked(jwtToken)) {
            throw new JwtTokenRevokedException("Token revoked");
        }
    }

    private void setAuthentication(HttpServletRequest request, String username) {
        SecurityContextHolder.getContext().setAuthentication(createAuthenticationToken(request,
                userService.loadUserByUsername(username)));
    }

    private static boolean authenticationIsNotPresent() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private static boolean isContainsJwtToken(String header) {
        return header != null && header.startsWith(BEARER);
    }

    private static UsernamePasswordAuthenticationToken createAuthenticationToken(
            HttpServletRequest request, ApiUser user
    ) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return token;
    }
}
