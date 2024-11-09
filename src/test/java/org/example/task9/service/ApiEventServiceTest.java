package org.example.task9.service;


import org.example.task5.integration.KudaGoServiceClient;
import org.example.task8.currency.converter.CurrencyConverter;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.service.CurrencyService;
import org.example.task9.exception.DateBoundsException;
import org.example.task9.model.ApiEvent;
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

class ApiEventServiceTest {

    @Mock
    private KudaGoServiceClient kudaGoServiceClient;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private EventService eventService;

    private ApiEvent apiEvent1;
    private ApiEvent apiEvent2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        apiEvent1 = new ApiEvent(1L, "Event 1", "100.0", false, null, null);
        apiEvent2 = new ApiEvent(2L, "Event 2", "200.0", false,null, null);
    }

    @Test
    void getEventsReactive_validRequest_shouldReturnFilteredEvents() {
        // Arrange
        LocalDate dateFrom = LocalDate.now();
        LocalDate dateTo = dateFrom.plusDays(7);
        String currencyCode = "USD";
        Double budget = 150.0;

        List<ApiEvent> apiEvents = Arrays.asList(apiEvent1, apiEvent2);
        CurrencyInfoDto currencyInfoDto = new CurrencyInfoDto(currencyCode, "1.0");

        when(kudaGoServiceClient.getEventsReactive(dateFrom, dateTo)).thenReturn(Mono.just(apiEvents));
        when(currencyService.getCurrencyInfoReactive(currencyCode)).thenReturn(Mono.just(currencyInfoDto));
        when(currencyConverter.getPriceInRubles(currencyInfoDto, budget)).thenReturn(BigDecimal.valueOf(150));

        // Act
        Mono<List<ApiEvent>> resultMono = eventService.getEventsReactive(budget, currencyCode, dateFrom, dateTo);
        List<ApiEvent> result = resultMono.block();

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

        List<ApiEvent> apiEvents = Arrays.asList(apiEvent1, apiEvent2);
        CurrencyInfoDto currencyInfoDto = new CurrencyInfoDto(currencyCode, "1.0");
        LocalDate today = LocalDate.now();
        var dateFrom = today.minusDays(today.getDayOfWeek().getValue() - 1);
        var dateTo = today.plusDays(7 - today.getDayOfWeek().getValue());

        when(kudaGoServiceClient.getEventsReactive(dateFrom, dateTo)).thenReturn(Mono.just(apiEvents));
        when(currencyService.getCurrencyInfoReactive(currencyCode)).thenReturn(Mono.just(currencyInfoDto));
        when(currencyConverter.getPriceInRubles(currencyInfoDto, budget)).thenReturn(BigDecimal.valueOf(150));

        // Act
        Mono<List<ApiEvent>> resultMono = eventService.getEventsReactive(budget, currencyCode, null, null);
        List<ApiEvent> result = resultMono.block();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}