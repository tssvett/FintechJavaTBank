package org.example.task9.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.task8.currency.validator.ValidCurrency;
import org.example.task9.model.Event;
import org.example.task9.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @SneakyThrows
    @GetMapping
    public List<Event> getEvents(@Valid @Positive @RequestParam Double budget,
                                 @Valid @ValidCurrency @RequestParam String currency,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate dateFrom,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate dateTo) {
        return eventService.getEventsReactive(budget, currency, dateFrom, dateTo).toFuture().get();
    }
}
