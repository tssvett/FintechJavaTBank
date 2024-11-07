package org.example.task9.advice;


import lombok.RequiredArgsConstructor;
import org.example.task8.advice.details.ExceptionDetails;
import org.example.task9.exception.DateBoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class EventExceptionHandler {

    @ExceptionHandler(DateBoundsException.class)
    public ResponseEntity<ExceptionDetails> handleException(DateBoundsException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ), HttpStatus.NOT_FOUND);
    }
}

