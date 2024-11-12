package org.example.task10.dto;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record EventReadDto(
        @Nullable String name,
        @Nullable String place,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Nullable LocalDate fromDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Nullable LocalDate toDate
) {
}
