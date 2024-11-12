package org.example.task5.handler.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ExceptionDetails(
        String exceptionName,
        String exceptionClass,
        String exceptionMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime exceptionTime
) {
}
