package org.example.task5.handler;

import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.exception.KudaGoException;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.handler.details.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KudaGoException.class)
    public ResponseEntity<ExceptionDetails> handleException(KudaGoException exception) {
        return new ResponseEntity<>(new ExceptionDetails(
                KudaGoException.class.getSimpleName(),
                KudaGoException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotExistException.class)
    public ResponseEntity<ExceptionDetails> handleException(CategoryNotExistException exception) {
        return new ResponseEntity<>(new ExceptionDetails(
                CategoryNotExistException.class.getSimpleName(),
                CategoryNotExistException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LocationNotExistException.class)
    public ResponseEntity<ExceptionDetails> handleException(LocationNotExistException exception) {
        return new ResponseEntity<>(new ExceptionDetails(
                LocationNotExistException.class.getSimpleName(),
                LocationNotExistException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}