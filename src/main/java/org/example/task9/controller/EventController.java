package org.example.task9.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.task10.dto.EventCreateDto;
import org.example.task10.dto.EventReadDto;
import org.example.task10.dto.EventUpdateDto;
import org.example.task10.entity.Event;
import org.example.task10.service.crud.CrudService;
import org.example.task8.currency.validator.ValidCurrency;
import org.example.task9.model.ApiEvent;
import org.example.task9.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final CrudService<Long, Event, EventCreateDto, EventReadDto, EventUpdateDto> eventCrudServiceImpl;

    @SneakyThrows
    @GetMapping("/api/v1/events")
    public List<ApiEvent> getEvents(@Valid @Positive @RequestParam Double budget,
                                    @Valid @ValidCurrency @RequestParam String currency,
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                    @RequestParam(required = false) LocalDate dateFrom,
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                    @RequestParam(required = false) LocalDate dateTo) {
        return eventService.getEventsReactive(budget, currency, dateFrom, dateTo).toFuture().get();
    }


    @PostMapping("/api/v1/events")
    public Long create(@Valid @RequestBody EventCreateDto eventCreateDto) {
        return eventCrudServiceImpl.create(eventCreateDto);
    }

    @DeleteMapping("/api/v1/events/{id}")
    public void delete(@PathVariable Long id) {
        eventCrudServiceImpl.delete(id);
    }

    @GetMapping("/api/v1/events/{id}")
    public Event read(@PathVariable Long id) {
        return eventCrudServiceImpl.read(id);
    }

    @PostMapping("/api/v1/events/filter")
    public List<Event> readAll(@Valid @RequestBody EventReadDto eventReadDto) {
        return eventCrudServiceImpl.readAll(eventReadDto);
    }

    @PutMapping("/api/v1/events")
    public Event update(@Valid @RequestBody EventUpdateDto eventUpdateDto) {
        return eventCrudServiceImpl.update(eventUpdateDto);
    }
}
