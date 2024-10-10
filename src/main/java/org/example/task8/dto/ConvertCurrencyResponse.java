package org.example.task8.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ConvertCurrencyResponse(
        @NotBlank(message = "field 'fromCurrency' cannot be empty")
        String fromCurrency,

        @NotBlank(message = "field 'toCurrency' cannot be empty")
        String toCurrency,

        @NotNull(message = "field 'convertedAmount' cannot be null")
        @Min(value = 0, message = "field 'convertedAmount' must be greater than 0")
        BigDecimal convertedAmount
) {
}
