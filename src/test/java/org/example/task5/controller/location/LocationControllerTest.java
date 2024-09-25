package org.example.task5.controller.location;

import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.model.Location;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationControllerTest {

    @Mock
    private KudaGoService<String, Location, LocationCreateDto, LocationUpdateDto> locationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        Location location1 = new Location("location-1", "Location 1");
        Location location2 = new Location("location-2", "Location 2");
        List<Location> locations = Arrays.asList(location1, location2);

        when(locationService.getAll()).thenReturn(locations);

        // Act
        List<Location> result = locationController.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(location1));
        assertTrue(result.contains(location2));
        verify(locationService).getAll();
    }

    @Test
    void getById_existingId() {
        // Arrange
        String id = "location-1";
        Location location = new Location(id, "Location 1");

        when(locationService.getById(id)).thenReturn(location);

        // Act
        Location result = locationController.getById(id);

        // Assert
        assertEquals(location, result);
        verify(locationService).getById(id);
    }

    @Test
    void getById_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(locationService.getById(id)).thenThrow(new LocationNotExistException("Location with id " + id + " does not exist"));

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.getById(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).getById(id);
    }

    @Test
    void create() {
        // Arrange
        LocationCreateDto dto = new LocationCreateDto("location-1", "Location 1");
        Location createdLocation = new Location(dto.slug(), dto.name());

        when(locationService.create(dto)).thenReturn(createdLocation);

        // Act
        Location result = locationController.create(dto);

        // Assert
        assertEquals(createdLocation, result);
        verify(locationService).create(dto);
    }

    @Test
    void update_existingId() {
        // Arrange
        String id = "location-1";
        LocationUpdateDto dto = new LocationUpdateDto("slug", "Updated Location");

        when(locationService.update(eq(id), any())).thenReturn(new Location(id, dto.name()));

        // Act
        Location result = locationController.update(id, dto);

        // Assert
        assertEquals(dto.name(), result.name());
        verify(locationService).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(locationService.update(eq(id), any())).thenThrow(new LocationNotExistException("Location with id " + id + " does not exist"));

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.update(id, new LocationUpdateDto("slug", "Some Name"));
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).update(eq(id), any());
    }


    @Test
    void delete_nonExistingId() {
        // Arrange
        String id = "location-99";

        doThrow(new LocationNotExistException("Location with id " + id + " does not exist")).when(locationService).delete(id);

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationController.delete(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(locationService).delete(id);
    }
}