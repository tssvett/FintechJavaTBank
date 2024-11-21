package org.example.task10.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import org.example.task10.dto.EventCreateDto;
import org.example.task10.dto.EventReadDto;
import org.example.task10.entity.Event;
import org.example.task10.entity.Place;
import org.example.task10.specification.criteria.SearchCriteria;
import org.example.task5.model.ApiLocation;
import org.example.task9.model.ApiEvent;

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
                null,
                date
        );
    }

    public static Event toEvent(EventCreateDto eventCreateDto, Place place) {
        return new Event(
                null,
                place,
                eventCreateDto.name(),
                null,
                eventCreateDto.date()
        );
    }

    public static Event toEvent(ApiEvent apiEvent) {
        return new Event(apiEvent.id(),
                Mapper.toPlace(apiEvent.place()),
                apiEvent.title(),
                extractPrice(apiEvent.price()),
                LocalDate.ofInstant(apiEvent.dates().get(0).start(), TimeZone.getDefault().toZoneId()));
    }

    public static SearchCriteria toSearchCriteria(EventReadDto eventReadDto) {
        return new SearchCriteria(
                eventReadDto.name(),
                eventReadDto.place(),
                eventReadDto.fromDate(),
                eventReadDto.toDate()
        );
    }

    public static Place toPlace(ApiLocation location) {
        return new Place(
                null,
                location.slug(),
                location.name(),
                null
        );
    }

    public static EventCreateDto toEventCreateDto(ApiEvent apiEvent, Place place) {
        return new EventCreateDto(
                apiEvent.title(),
                LocalDate.ofInstant(apiEvent.dates().get(0).start(), TimeZone.getDefault().toZoneId()),
                place.getId()
        );
    }

    private static BigDecimal extractPrice(String price) {
        BigDecimal priceValue;
        if (price == null || price.isEmpty()) {
            priceValue = BigDecimal.ZERO; // Use 0 if the price string is empty
        } else {
            priceValue = BigDecimal.valueOf(Long.parseLong(price));
        }
        return priceValue;
    }


}
