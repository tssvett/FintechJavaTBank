package org.example.task10.advice;

import jakarta.persistence.EntityNotFoundException;
import org.example.task10.exception.EntityDeleteException;
import org.example.task10.exception.PlaceNotFoundException;
import org.example.task8.advice.details.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handlePlaceNotFoundException(PlaceNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityDeleteException.class)
    public ResponseEntity<ExceptionDetails> handleEntityDeleteException(EntityDeleteException ex) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }
}