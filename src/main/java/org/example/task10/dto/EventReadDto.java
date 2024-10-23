package org.example.task10.dto;

import java.time.LocalDate;

public record EventReadDto(
        String name,
        String place,
        LocalDate fromDate,
        LocalDate toDate
) {
}
