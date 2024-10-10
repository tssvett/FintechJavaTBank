package org.example.task8.currency.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.task8.exception.ValuteNotExistException;

import java.util.Currency;


public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            throw new ValuteNotExistException("Currency code cannot be null or empty");
        }

        try {
            Currency currency = Currency.getInstance(value);
            return true; // Valid currency code
        } catch (IllegalArgumentException e) {
            throw new ValuteNotExistException("Invalid currency code: " + value);
        }
    }
}
