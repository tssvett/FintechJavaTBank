package org.example.task10.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EventUpdateDto(
        Long eventIdToUpdate,
        String name,
        LocalDate date,
        UUID placeId
) {
}
