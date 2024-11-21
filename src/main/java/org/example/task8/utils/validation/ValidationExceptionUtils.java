package org.example.task8.utils.validation;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ValidationExceptionUtils {
    private static final String EXCEPTIONS_DELIMITER = ",";

    public String extractExceptionMessages(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(EXCEPTIONS_DELIMITER));
    }
}
