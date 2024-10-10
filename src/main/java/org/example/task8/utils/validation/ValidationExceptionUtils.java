package org.example.task8.utils.validation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@Component
public class ValidationExceptionUtils {
    private final String EXCEPTIONS_DELIMITER = ",";

    public String extractExceptionMessages(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(EXCEPTIONS_DELIMITER));
    }
}
