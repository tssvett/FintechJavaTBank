package org.example.task8.currency.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.parser.xml.model.Valute;
import org.example.task8.utils.numbers.BigDecimalCurrencyParser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyConverter {
    private final BigDecimalCurrencyParser bigDecimalCurrencyParser;

    public BigDecimal convertValue(Valute fromValute, Valute toValute, BigDecimal amount) {

        BigDecimal fromValue = bigDecimalCurrencyParser.parseCurrencyValue(fromValute.value());
        BigDecimal toValue = bigDecimalCurrencyParser.parseCurrencyValue(toValute.value());
        BigDecimal result = amount.multiply(fromValue).divide(toValue, RoundingMode.HALF_UP);
        log.info("Convertion {} from {} to {} is {}", amount, fromValute.charCode(), toValute.charCode(), result);

        return result;
    }

    public BigDecimal getPriceInRubles(CurrencyInfoDto currencyInfoDto, Double budget) {
        return bigDecimalCurrencyParser.parseCurrencyValue(currencyInfoDto.rate()).multiply(BigDecimal.valueOf(budget));
    }
}
