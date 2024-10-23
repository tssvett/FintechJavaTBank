package org.example.task10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record EventReadDto(
        @NotBlank(message = "Name cannot be empty") String name,
        @NotBlank(message = "Place cannot be empty") String place,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull(message = "From date cannot be empty") LocalDate fromDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull(message = "To date cannot be empty") LocalDate toDate
) {
}
