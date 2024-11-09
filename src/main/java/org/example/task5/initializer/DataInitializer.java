package org.example.task5.initializer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.service.crud.impl.PlaceCrudServiceImpl;
import org.example.task11.pattern.observer.InitializerObservable;
import org.example.task11.pattern.observer.PlaceObserver;
import org.example.task5.exception.DataInitializationException;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.ApiLocation;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
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
    private final ExecutorService fixedThreadPool;
    private final InitializerObservable initializerObservable;
    private final PlaceObserver placeObserver;
    private final PlaceCrudServiceImpl placeCrudService;


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
        initializerObservable.registerObserver(placeObserver);
        initializerObservable.setPlacesList(placeCrudService.getPlacesFromIntegrations());
        initializerObservable.notifyObservers();
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