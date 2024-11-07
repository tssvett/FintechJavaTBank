package org.example.task12.advice;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.task12.security.exceptions.UserAlreadyRegisteredException;
import org.example.task8.advice.details.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(UserAlreadyRegisteredException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }
}
