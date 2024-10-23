package org.example.task10.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EventCreateDto(
        String name,
        LocalDate date,
        UUID placeId
) {
}
