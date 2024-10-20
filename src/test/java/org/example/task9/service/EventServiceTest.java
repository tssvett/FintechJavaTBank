package org.example.task9.service;


import org.example.task5.integration.KudaGoServiceClient;
import org.example.task8.currency.converter.CurrencyConverter;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.service.CurrencyService;
import org.example.task9.exception.DateBoundsException;
import org.example.task9.model.Event;
import org.example.task9.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class EventServiceTest {

    @Mock
    private KudaGoServiceClient kudaGoServiceClient;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private EventService eventService;

    private Event event1;
    private Event event2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        event1 = new Event(1L, "Event 1", "100.0", false);
        event2 = new Event(2L, "Event 2", "200.0", false);
    }

    @Test
    void getEventsReactive_validRequest_shouldReturnFilteredEvents() {
        // Arrange
        LocalDate dateFrom = LocalDate.now();
        LocalDate dateTo = dateFrom.plusDays(7);
        String currencyCode = "USD";
        Double budget = 150.0;

        List<Event> events = Arrays.asList(event1, event2);
        CurrencyInfoDto currencyInfoDto = new CurrencyInfoDto(currencyCode, "1.0");

        when(kudaGoServiceClient.getEventsReactive(dateFrom, dateTo)).thenReturn(Mono.just(events));
        when(currencyService.getCurrencyInfoReactive(currencyCode)).thenReturn(Mono.just(currencyInfoDto));
        when(currencyConverter.getPriceInRubles(currencyInfoDto, budget)).thenReturn(BigDecimal.valueOf(150));

        // Act
        Mono<List<Event>> resultMono = eventService.getEventsReactive(budget, currencyCode, dateFrom, dateTo);
        List<Event> result = resultMono.block();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).title());
    }

    @Test
    void getEventsReactive_dateToBeforeDateFrom_shouldThrowDateBoundsException() {
        // Arrange
        LocalDate dateFrom = LocalDate.now().plusDays(7);
        LocalDate dateTo = LocalDate.now();

        // Act & Assert
        assertThrows(DateBoundsException.class,
                () -> eventService.getEventsReactive(100.0, "USD", dateFrom, dateTo).block());
    }

    @Test
    void getEventsReactive_noDatesProvided_shouldUseDefaultDates() {
        // Arrange
        String currencyCode = "USD";
        Double budget = 150.0;

        List<Event> events = Arrays.asList(event1, event2);
        CurrencyInfoDto currencyInfoDto = new CurrencyInfoDto(currencyCode, "1.0");
        LocalDate today = LocalDate.now();
        var dateFrom = today.minusDays(today.getDayOfWeek().getValue() - 1);
        var dateTo = today.plusDays(7 - today.getDayOfWeek().getValue());

        when(kudaGoServiceClient.getEventsReactive(dateFrom, dateTo)).thenReturn(Mono.just(events));
        when(currencyService.getCurrencyInfoReactive(currencyCode)).thenReturn(Mono.just(currencyInfoDto));
        when(currencyConverter.getPriceInRubles(currencyInfoDto, budget)).thenReturn(BigDecimal.valueOf(150));

        // Act
        Mono<List<Event>> resultMono = eventService.getEventsReactive(budget, currencyCode, null, null);
        List<Event> result = resultMono.block();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}