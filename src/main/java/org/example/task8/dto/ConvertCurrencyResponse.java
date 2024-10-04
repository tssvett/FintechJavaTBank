package org.example.task8.dto;

public record ConvertCurrencyResponse(
        String fromCurrency,
        String toCurrency,
        double convertedAmount
) {
}
