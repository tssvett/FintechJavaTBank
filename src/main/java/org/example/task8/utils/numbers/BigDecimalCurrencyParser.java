package org.example.task8.utils.numbers;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BigDecimalCurrencyParser {
    public BigDecimal parseCurrencyValue(String value) {
        String sanitizedValue = value.replace(",", ".").trim();

        try {
            return new BigDecimal(sanitizedValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid currency value: " + value, e);
        }
    }
}
