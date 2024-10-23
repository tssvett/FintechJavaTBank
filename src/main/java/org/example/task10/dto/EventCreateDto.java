package org.example.task10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record EventCreateDto(
        @NotBlank String name,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate date,
        @NotNull UUID placeId
) {
}
