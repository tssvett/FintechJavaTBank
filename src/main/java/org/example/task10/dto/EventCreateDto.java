package org.example.task10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record EventCreateDto(
        @NotBlank(message = "field 'name' cannot be empty") String name,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull(message = "field 'date' cannot be empty") LocalDate date,

        @NotNull(message = "field 'placeId' cannot be empty") UUID placeId
) {
}
