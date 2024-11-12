package org.example.task8.currency.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.task8.exception.ValuteNotExistException;


public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            throw new ValuteNotExistException("Currency code cannot be null or empty");
        }

        try {
            return true;
        } catch (IllegalArgumentException e) {
            throw new ValuteNotExistException("Invalid currency code: " + value);
        }
    }
}
