package org.example.task8.dto;

public record ConvertCurrencyRequest(
        String fromCurrency,
        String toCurrency,
        double amount
) {
}
