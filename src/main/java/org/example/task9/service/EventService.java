package org.example.task9.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task8.currency.converter.CurrencyConverter;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.service.CurrencyService;
import org.example.task9.exception.DateBoundsException;
import org.example.task9.model.Event;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final KudaGoServiceClient kudaGoServiceClient;
    private final CurrencyService currencyService;
    private final CurrencyConverter currencyConverter;

    public CompletableFuture<List<Event>> getEvents(@NotNull Double budget, @NotNull String currencyCode,
                                                    LocalDate dateFrom, LocalDate dateTo) {
        Optional<LocalDate> dateFromOpt = Optional.ofNullable(dateFrom);
        Optional<LocalDate> dateToOpt = Optional.ofNullable(dateTo);

        if (dateFromOpt.isEmpty() || dateToOpt.isEmpty()) {
            LocalDate today = LocalDate.now();
            dateFrom = today.minusDays(today.getDayOfWeek().getValue() - 1);
            dateTo = today.plusDays(7 - today.getDayOfWeek().getValue());
        }

        if (dateTo.isBefore(dateFrom)) {
            throw new DateBoundsException("dateTo must be after dateFrom");
        }

        CompletableFuture<List<Event>> eventsFuture = kudaGoServiceClient.getEventsFuture(dateFrom, dateTo);
        CompletableFuture<CurrencyInfoDto> currencyInfoFuture = currencyService.getCurrencyInfoFuture(currencyCode);

        return eventsFuture.thenCombine(currencyInfoFuture, (events, currencyInfoDto) -> events.stream()
                .filter(event -> event.isFitsBudget(currencyConverter.getPriceInRubles(currencyInfoDto, budget)))
                .toList());
    }

    public Mono<List<Event>> getEventsReactive(@NotNull Double budget, @NotNull String currencyCode,
                                               LocalDate dateFrom, LocalDate dateTo) {
        // Set default dates if not provided
        if (dateFrom == null || dateTo == null) {
            LocalDate today = LocalDate.now();
            dateFrom = today.minusDays(today.getDayOfWeek().getValue() - 1);
            dateTo = today.plusDays(7 - today.getDayOfWeek().getValue());
        }

        // Validate date boundaries
        if (dateTo.isBefore(dateFrom)) {
            return Mono.error(new DateBoundsException("dateTo must be after dateFrom"));
        }

        // Fetch events and currency info reactively
        Mono<List<Event>> eventsMono = kudaGoServiceClient.getEventsReactive(dateFrom, dateTo);
        Mono<CurrencyInfoDto> currencyInfoMono = currencyService.getCurrencyInfoReactive(currencyCode);

        // Combine the results and filter based on budget
        return Mono.zip(eventsMono, currencyInfoMono)
                .flatMap(tuple -> {
                    List<Event> events = tuple.getT1();
                    CurrencyInfoDto currencyInfoDto = tuple.getT2();
                    BigDecimal budgetInRubles = currencyConverter.getPriceInRubles(currencyInfoDto, budget);

                    return Flux.fromIterable(events)
                            .filter(event -> event.isFitsBudget(budgetInRubles))
                            .collectList();
                });
    }
}