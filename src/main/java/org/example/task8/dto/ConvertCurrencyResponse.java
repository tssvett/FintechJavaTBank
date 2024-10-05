package org.example.task8.dto;

import java.math.BigDecimal;

public record ConvertCurrencyResponse(
        String fromCurrency,
        String toCurrency,
        BigDecimal convertedAmount
) {
}
