package org.example.task8.dto;

import jakarta.validation.constraints.NotBlank;

public record CurrencyInfoDto(
        @NotBlank(message = "field 'currency' cannot be empty") String currency,
        @NotBlank(message = "field 'rate' cannot be empty") String rate
) {
}
