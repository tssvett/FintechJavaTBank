package org.example.task5.controller.location;


import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.model.Location;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private KudaGoService<String, Location, LocationCreateDto, LocationUpdateDto> locationService;

    @InjectMocks
    private LocationController locationController;


    Location location1;
    LocationCreateDto locationCreateDto1;
    LocationUpdateDto locationUpdateDto1;
    Location location2;
    LocationCreateDto locationCreateDto2;
    LocationUpdateDto locationUpdateDto2;

    @BeforeEach
    void before() {
        location1 = new Location("location-1", "Location 1");
        locationCreateDto1 = new LocationCreateDto("location-1", "Location 1");
        locationUpdateDto1 = new LocationUpdateDto("updatedLocation-1", "updatedLocation 1");

        location2 = new Location("location-2", "Location 2");
        locationCreateDto2 = new LocationCreateDto("location-2", "Category 2");
        locationUpdateDto2 = new LocationUpdateDto("updatedLocation-2", "updatedLocation 2");
    }

    @Test
    void getAll_notEmptyDatasource_shouldReturnNotEmptyList() {
        // Arrange
        List<Location> categories = Arrays.asList(location1, location2);
        when(locationService.getAll()).thenReturn(categories);

        // Act
        List<Location> result = locationController.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(location1));
        assertTrue(result.contains(location2));
        verify(locationService).getAll();
    }


    @Test
    void getAll_emptyDatasource_shouldReturnEmptyList() {
        // Arrange
        when(locationService.getAll()).thenReturn(List.of());

        // Act
        List<Location> result = locationController.getAll();

        // Assert
        assertEquals(0, result.size());
        verify(locationService).getAll();
    }

    @Test
    void getById_existingId_shouldReturnCategory() {
        // Arrange
        String id = "location-1";
        when(locationService.getById(id)).thenReturn(location1);

        // Act
        Location result = locationController.getById(id);

        // Assert
        assertEquals(location1, result);
        verify(locationService).getById(id);
    }

    @Test
    void getById_notExistingId_shouldThrowException() {
        // Arrange
        String id = "location-99";

        when(locationService.getById(id)).thenThrow(new LocationNotExistException("Location with id " + id + " does not exist"));

        // Act & Assert
        var exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.getById(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).getById(id);
    }

    @Test
    void create_shouldReturnCreatedCategory() {
        // Arrange
        when(locationService.create(locationCreateDto1)).thenReturn(location1);

        // Act
        Location result = locationController.create(locationCreateDto1);

        // Assert
        assertEquals(locationCreateDto1.name(), result.name());
        assertEquals(locationCreateDto1.slug(), result.slug());
        verify(locationService).create(locationCreateDto1);
    }

    @Test
    void update_existingId() {
        // Arrange
        String id = "location-1";

        when(locationService.update(id, locationUpdateDto1)).thenReturn(new Location(locationUpdateDto1.slug(), locationUpdateDto1.name()));

        // Act
        Location result = locationController.update(id, locationUpdateDto1);

        // Assert
        assertEquals(locationUpdateDto1.name(), result.name());
        verify(locationService).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(locationService.update(eq(id), any())).thenThrow(new LocationNotExistException("Location with id " + id + " does not exist"));

        // Act & Assert
        var exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.update(id, locationUpdateDto1);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).update(eq(id), any());
    }

    @Test
    void delete_existingId() {
        // Arrange
        String id = "location-1";

        when(locationService.delete(id)).thenReturn(location1);

        // Act
        locationController.delete(id);

        // Assert
        verify(locationService).delete(id);
        verifyNoMoreInteractions(locationService);
    }


    @Test
    void delete_nonExistingId() {
        // Arrange
        String id = "location-99";

        doThrow(new LocationNotExistException("Location with id " + id + " does not exist")).when(locationService).delete(id);

        // Act & Assert
        var exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.delete(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).delete(id);
    }
}