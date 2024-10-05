package org.example.task8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.currency.converter.CurrencyConverter;
import org.example.task8.dto.ConvertCurrencyRequest;
import org.example.task8.dto.ConvertCurrencyResponse;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.exception.ValuteNotFoundException;
import org.example.task8.integration.CurrencyRateServiceClient;
import org.example.task8.mapper.CurrencyMapper;
import org.example.task8.parser.xml.model.Valute;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRateServiceClient currencyRateServiceClient;
    private final CurrencyConverter currencyConverter;
    private final CurrencyMapper currencyMapper;

    public CurrencyInfoDto getCurrencyInfo(String code) {
        return currencyRateServiceClient.getCurrencyRates()
                .stream()
                .filter(v -> v.charCode().equals(code))
                .findFirst()
                .map(currencyMapper::toCurrencyInfoDto)
                .orElseThrow(() -> new ValuteNotFoundException("Currency with code " + code + " does not exist"));
    }

    public ConvertCurrencyResponse convertCurrency(ConvertCurrencyRequest convertCurrencyRequest) {
        List<Valute> valuteList = currencyRateServiceClient.getCurrencyRates();

        Valute fromValute = getValuteByCode(valuteList, convertCurrencyRequest.fromCurrency());
        Valute toValute = getValuteByCode(valuteList, convertCurrencyRequest.toCurrency());
        BigDecimal amount = BigDecimal.valueOf(convertCurrencyRequest.amount());

        BigDecimal convertionResult = currencyConverter.convertValue(fromValute, toValute, amount);
        return new ConvertCurrencyResponse(convertCurrencyRequest.fromCurrency(), convertCurrencyRequest.toCurrency(), convertionResult);
    }


    private Valute getValuteByCode(List<Valute> valuteList, String code) {
        return valuteList
                .stream()
                .filter(v -> v.charCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ValuteNotFoundException("Currency with code " + code + " does not exist"));
    }
}
