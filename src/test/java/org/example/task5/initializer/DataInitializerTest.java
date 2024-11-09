package org.example.task5.initializer;

import org.example.task5.exception.DataInitializationException;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.model.Category;
import org.example.task5.model.Location;
import org.example.task5.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private KudaGoServiceClient kudaGoServiceClient;

    @Mock
    private InMemoryRepository<Integer, Category> categoryRepository;

    @Mock
    private InMemoryRepository<String, Location> locationRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    static Stream<Arguments> generateData() {
        List<Category> fullCategoriesList = List.of(
                new Category(1, "category-1", "Category 1"),
                new Category(2, "category-2", "Category 2"),
                new Category(3, "category-3", "Category 3")
        );

        List<Location> fullLocationsList = List.of(
                new Location("location-1", "Location 1"),
                new Location("location-2", "Location 2"),
                new Location("location-3", "Location 3")
        );
        return Stream.of(
                Arguments.of(fullCategoriesList, fullLocationsList)
        );
    }

    @ParameterizedTest
    @MethodSource("generateData")
    void initializeData_successKudaGoServiceClient_successfullyInitialized(List<Category> categories, List<Location> locations) {
        //Arrange
        when(kudaGoServiceClient.getCategories()).thenReturn(categories);
        when(kudaGoServiceClient.getLocations()).thenReturn(locations);

        //Act && Assert
        assertDoesNotThrow(() -> dataInitializer.threadingInitializeData());
    }

    @Test
    void initializeData_failureGetCategories_throwsException() {
        //Arrange
        when(kudaGoServiceClient.getCategories()).thenThrow(RuntimeException.class);

        //Act && Assert
        assertThrows(DataInitializationException.class, () -> dataInitializer.threadingInitializeData());
    }

    @Test
    void initializeData_failureGetLocations_throwsException() {
        //Arrange
        when(kudaGoServiceClient.getLocations()).thenThrow(RuntimeException.class);

        //Act && Assert
        assertThrows(DataInitializationException.class, () -> dataInitializer.threadingInitializeData());
    }
}