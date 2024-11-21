package org.example.task10.service.crud.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.entity.Event;
import org.example.task10.entity.Place;
import org.example.task10.exception.EntityDeleteException;
import org.example.task10.exception.PlaceNotFoundException;
import org.example.task10.repository.PlaceRepository;
import org.example.task10.utils.Mapper;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.model.ApiLocation;
import org.example.task9.model.ApiEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceCrudServiceImpl {
    private final PlaceRepository placeRepository;
    private final KudaGoServiceClient kudaGoServiceClient;

    public Place read(UUID id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Event not found with id: " + id));
    }

    public void delete(UUID id) {
        try {
            placeRepository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeleteException("Failed to delete place with id: " + id);
        }
    }

    public List<Place> getAll() {
        return placeRepository.findAll();
    }

    public List<Place> getPlacesFromIntegrations() {
        log.info("Start initializing data");

        List<ApiLocation> locations = kudaGoServiceClient.getLocations();
        List<Place> placeList = locations.stream()
                .map(Mapper::toPlace)
                .toList();

        for (Place place : placeList) {
            List<ApiEvent> apiEvents = fetchApiEventsForPlace(place);
            List<Event> events = createEventsFromApiEvents(apiEvents, place);
            place.setEvents(events);
        }

        return placeList;
    }

    private List<ApiEvent> fetchApiEventsForPlace(Place place) {
        return kudaGoServiceClient.getEventsFuture(
                        LocalDate.of(2010, 1, 1),
                        LocalDate.of(2024, 1, 1),
                        place.getSlug())
                .join();
    }

    private List<Event> createEventsFromApiEvents(List<ApiEvent> apiEvents, Place place) {
        List<Event> eventList = new ArrayList<>();

        for (ApiEvent apiEvent : apiEvents) {
            log.debug("Event: {}", apiEvent.toString());

            Event event = new Event(
                    apiEvent.id(),
                    place,
                    apiEvent.title(),
                    extractPrice(apiEvent.price()),
                    LocalDate.ofInstant(apiEvent.dates().get(0).start(), TimeZone.getDefault().toZoneId())
            );

            eventList.add(event);
        }

        return eventList;
    }

    private static BigDecimal extractPrice(String price) {
        BigDecimal priceValue;
        if (price == null || price.isEmpty()) {
            priceValue = BigDecimal.ZERO;
        } else {
            priceValue = BigDecimal.valueOf(Long.parseLong(price));
        }
        return priceValue;
    }
}
