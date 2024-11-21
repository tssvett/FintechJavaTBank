package org.example.task10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

public record EventUpdateDto(
        @NotNull(message = "eventIdToUpdate cannot be null") Long eventIdToUpdate,
        @NotBlank(message = "name cannot be empty") String name,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull(message = "date cannot be null") LocalDate date,

        @NotNull(message = "placeId cannot be null") UUID placeId
) {
}
