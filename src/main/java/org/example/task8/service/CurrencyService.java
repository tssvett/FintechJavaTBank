package org.example.task8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.currency.converter.CurrencyConverter;
import org.example.task8.dto.ConvertCurrencyRequest;
import org.example.task8.dto.ConvertCurrencyResponse;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.exception.ValuteNotExistException;
import org.example.task8.exception.ValuteNotFoundException;
import org.example.task8.integration.CurrencyRateServiceClient;
import org.example.task8.mapper.CurrencyMapper;
import org.example.task8.parser.xml.model.Valute;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                .orElseThrow(() -> new ValuteNotFoundException("Cant find currency with code: " + code + " in cb response"));
    }

    public CompletableFuture<CurrencyInfoDto> getCurrencyInfoFuture(String code) {
        return currencyRateServiceClient.getCurrencyRatesFuture()
                .thenApply(currency -> currency.stream()
                        .filter(v -> v.charCode().equals(code))
                        .findFirst()
                        .map(currencyMapper::toCurrencyInfoDto)
                        .orElseThrow(() -> new ValuteNotFoundException("Cant find currency with code: " + code + " in cb response")));
    }

    public Mono<CurrencyInfoDto> getCurrencyInfoReactive(String code) {
        return currencyRateServiceClient.getCurrencyRatesReactive()
                .flatMap(currencyRates ->
                        Flux.fromIterable(currencyRates)
                                .filter(v -> v.charCode().equals(code))
                                .single() // Получаем единственный элемент или выбрасываем ошибку, если не найден
                                .map(currencyMapper::toCurrencyInfoDto)
                                .switchIfEmpty(Mono.error(new ValuteNotFoundException("Can't find currency with code: " + code + " in cb response")))
                );
    }

    public ConvertCurrencyResponse convertCurrency(ConvertCurrencyRequest convertCurrencyRequest) {
        List<Valute> valuteList = currencyRateServiceClient.getCurrencyRates();

        Valute fromValute = getValuteByCode(valuteList, convertCurrencyRequest.fromCurrency());
        Valute toValute = getValuteByCode(valuteList, convertCurrencyRequest.toCurrency());
        BigDecimal amount = convertCurrencyRequest.amount();

        BigDecimal convertionResult = currencyConverter.convertValue(fromValute, toValute, amount);
        return new ConvertCurrencyResponse(convertCurrencyRequest.fromCurrency(), convertCurrencyRequest.toCurrency(), convertionResult);
    }

    public CompletableFuture<ConvertCurrencyResponse> convertCurrencyFuture(ConvertCurrencyRequest convertCurrencyRequest) {
        return currencyRateServiceClient.getCurrencyRatesFuture()
                .thenApply(valuteList -> {
                    Valute fromValute = getValuteByCode(valuteList, convertCurrencyRequest.fromCurrency());
                    Valute toValute = getValuteByCode(valuteList, convertCurrencyRequest.toCurrency());
                    BigDecimal amount = convertCurrencyRequest.amount();

                    BigDecimal conversionResult = currencyConverter.convertValue(fromValute, toValute, amount);
                    return new ConvertCurrencyResponse(convertCurrencyRequest.fromCurrency(), convertCurrencyRequest.toCurrency(), conversionResult);
                });
    }

    public Mono<ConvertCurrencyResponse> convertCurrencyReactive(ConvertCurrencyRequest convertCurrencyRequest) {
        return currencyRateServiceClient.getCurrencyRatesReactive()
                .flatMap(valuteList -> {
                    Valute fromValute = getValuteByCode(valuteList, convertCurrencyRequest.fromCurrency());
                    Valute toValute = getValuteByCode(valuteList, convertCurrencyRequest.toCurrency());
                    BigDecimal amount = convertCurrencyRequest.amount();

                    // Проверка на наличие валют
                    if (fromValute == null || toValute == null) {
                        return Mono.error(new ValuteNotFoundException("One of the currencies was not found."));
                    }

                    BigDecimal conversionResult = currencyConverter.convertValue(fromValute, toValute, amount);
                    return Mono.just(new ConvertCurrencyResponse(convertCurrencyRequest.fromCurrency(), convertCurrencyRequest.toCurrency(), conversionResult));
                });
    }




    private Valute getValuteByCode(List<Valute> valuteList, String code) {
        return valuteList
                .stream()
                .filter(v -> v.charCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new ValuteNotExistException("Currency with code " + code + " does not exist"));
    }
}
