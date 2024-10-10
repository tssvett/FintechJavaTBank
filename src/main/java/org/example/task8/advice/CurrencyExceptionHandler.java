package org.example.task8.advice;

import lombok.RequiredArgsConstructor;
import org.example.task8.advice.details.ExceptionDetails;
import org.example.task8.exception.CurrencyClientException;
import org.example.task8.exception.ServiceUnavailableException;
import org.example.task8.exception.ValuteNotExistException;
import org.example.task8.exception.ValuteNotFoundException;
import org.example.task8.exception.XmlParserException;
import org.example.task8.utils.validation.ValidationExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class CurrencyExceptionHandler {
    private final ValidationExceptionUtils validationExceptionUtils;

    @ExceptionHandler(CurrencyClientException.class)
    public ResponseEntity<ExceptionDetails> handleException(CurrencyClientException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionDetails> handleException(ServiceUnavailableException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "3600");

        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        exception.getMessage()
                ), headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ValuteNotExistException.class)
    public ResponseEntity<ExceptionDetails> handleException(ValuteNotExistException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValuteNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleException(ValuteNotFoundException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(XmlParserException.class)
    public ResponseEntity<ExceptionDetails> handleException(XmlParserException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        exception.getMessage()
                ), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        validationExceptionUtils.extractExceptionMessages(exception)
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDetails> handleException(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }
}
