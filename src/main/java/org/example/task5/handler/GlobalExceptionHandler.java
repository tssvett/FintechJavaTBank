package org.example.task5.handler;

import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.exception.KudaGoException;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.handler.details.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KudaGoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDetails handleException(KudaGoException exception) {
        return new ExceptionDetails(
                KudaGoException.class.getSimpleName(),
                KudaGoException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(CategoryNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDetails handleException(CategoryNotExistException exception) {
        return new ExceptionDetails(
                CategoryNotExistException.class.getSimpleName(),
                CategoryNotExistException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(LocationNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDetails handleException(LocationNotExistException exception) {
        return new ExceptionDetails(
                LocationNotExistException.class.getSimpleName(),
                LocationNotExistException.class.getName(),
                exception.getMessage(),
                LocalDateTime.now()
        );
    }
}