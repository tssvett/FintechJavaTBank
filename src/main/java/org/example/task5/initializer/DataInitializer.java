package org.example.task5.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.Category;
import org.example.task5.model.Location;
import org.example.task5.repository.InMemoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements Initializer {
    private final KudaGoServiceClient kudaGoServiceClient;
    private final InMemoryRepository<Integer, Category> categoryRepository;
    private final InMemoryRepository<String, Location> locationRepository;


    @LogExecutionTime
    public void initializeData() {
        log.info("Start initialization data");
        try {
            List<Category> categories = kudaGoServiceClient.getCategories();
            for (Category category : categories) {
                categoryRepository.save(category.id(), category);
            }
            log.info("Categories successfully initialized");

            List<Location> locations = kudaGoServiceClient.getLocations();
            for (Location location : locations) {
                locationRepository.save(location.slug(), location);
            }
            log.info("Locations successfully initialized");
        } catch (Exception e) {
            log.error("Error while initializing data: {}", e.getMessage());
        }
    }
}