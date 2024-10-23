package org.example.task10.utils;

import lombok.experimental.UtilityClass;
import org.example.task10.dto.EventCreateDto;
import org.example.task10.dto.EventReadDto;
import org.example.task10.enitiy.Event;
import org.example.task10.enitiy.Place;
import org.example.task10.specification.criteria.SearchCriteria;

import java.time.LocalDate;

@UtilityClass
public class Mapper {

    public static EventReadDto toEventReadDto(Event event, LocalDate fromDate, LocalDate toDate) {
        return new EventReadDto(
                event.getName(),
                event.getPlace().getName(),
                fromDate,
                toDate
        );
    }

    public static Event toEvent(EventReadDto eventReadDto, Long id, Place place, LocalDate date) {
        return new Event(
                id,
                place,
                eventReadDto.name(),
                date
        );
    }

    public static Event toEvent(EventCreateDto eventCreateDto, Place place) {
        return new Event(
                null,
                place,
                eventCreateDto.name(),
                eventCreateDto.date()
        );
    }

    public static SearchCriteria toSearchCriteria(EventReadDto eventReadDto) {
        return new SearchCriteria(
                eventReadDto.name(),
                eventReadDto.place(),
                eventReadDto.fromDate(),
                eventReadDto.toDate()
        );
    }
}
