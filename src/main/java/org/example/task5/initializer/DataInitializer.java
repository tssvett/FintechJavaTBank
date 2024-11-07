package org.example.task5.initializer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.entity.Event;
import org.example.task10.entity.Place;
import org.example.task10.repository.EventRepository;
import org.example.task10.repository.PlaceRepository;
import org.example.task10.utils.Mapper;
import org.example.task5.exception.DataInitializationException;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.ApiLocation;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.example.task9.model.ApiEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements Initializer {
    private final KudaGoServiceClient kudaGoServiceClient;
    private final InMemoryRepository<Integer, Category> categoryMapRepository;
    private final InMemoryRepository<String, ApiLocation> locationMapRepository;
    private final PlaceRepository placeRepository;
    private final EventRepository eventRepository;
    private final ExecutorService fixedThreadPool;


    @LogExecutionTime
    public void threadingInitializeData() {
        log.info("Start initialization data");
        try {
            CountDownLatch latch = new CountDownLatch(2);

            fixedThreadPool.submit(() -> {
                try {
                    List<Category> categories = kudaGoServiceClient.getCategories();
                    saveCategories(categories);
                    log.info("Categories successfully initialized");
                } finally {
                    latch.countDown();
                }
            });

            fixedThreadPool.submit(() -> {
                try {
                    List<ApiLocation> locations = kudaGoServiceClient.getLocations();
                    saveLocations(locations);
                    log.info("Locations successfully initialized");
                } finally {
                    latch.countDown();
                }
            });

            latch.await();
        } catch (Exception e) {
            log.error("Error while initializing data: {}", e.getMessage());
            throw new DataInitializationException(e.getMessage());
        }
    }

    @LogExecutionTime
    public void initializeData() {
        log.info("Start initialization data");
        try {
            List<Category> categories = kudaGoServiceClient.getCategories();
            saveCategories(categories);
            log.info("Categories successfully initialized");

            List<ApiLocation> locations = kudaGoServiceClient.getLocations();
            saveLocations(locations);
            log.info("Locations successfully initialized");
        } catch (Exception e) {
            log.error("Error while initializing data: {}", e.getMessage());
            throw new DataInitializationException(e.getMessage());
        }
    }

    public void initializeDatabase() {
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
        try {
            placeRepository.saveAll(placeList);
            log.info("Data successfully initialized");
        } catch (Exception e) {
            log.warn("Founded not unique keys, initialization failed: {}", e.getMessage());
        }
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

    @PreDestroy
    public void onApplicationShutdown() {
        fixedThreadPool.shutdown();

        try {
            if (!fixedThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                fixedThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveLocations(List<ApiLocation> locations) {
        for (ApiLocation location : locations) {
            locationMapRepository.save(location.slug(), location);
        }
    }

    private void saveCategories(List<Category> categories) {
        for (Category category : categories) {
            categoryMapRepository.save(category.id(), category);
        }
    }
}