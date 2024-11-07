package org.example.task5.service.location;

import org.example.task11.pattern.mementopattern.catetaker.LocationHistory;
import org.example.task11.pattern.mementopattern.memento.LocationMemento;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.model.ApiLocation;
import org.example.task5.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.stream.Location;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApiLocationServiceTest {

    @Mock
    private InMemoryRepository<String, ApiLocation> inMemoryRepository;

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationHistory locationHistory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        ApiLocation location1 = new ApiLocation("location-1", "Location 1");
        ApiLocation location2 = new ApiLocation("location-2", "Location 2");
        HashMap<String, ApiLocation> locations = new HashMap<>();
        locations.put(location1.slug(), location1);
        locations.put(location2.slug(), location2);

        when(inMemoryRepository.findAll()).thenReturn(locations);

        // Act
        List<ApiLocation> result = locationService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(location1));
        assertTrue(result.contains(location2));
        verify(inMemoryRepository).findAll();
    }

    @Test
    void getById_existingId() {
        // Arrange
        String id = "location-1";
        ApiLocation location = new ApiLocation(id, "Location 1");

        when(inMemoryRepository.findById(id)).thenReturn(Optional.of(location));

        // Act
        ApiLocation result = locationService.getById(id);

        // Assert
        assertEquals(location, result);
        verify(inMemoryRepository).findById(id);
    }

    @Test
    void getById_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(inMemoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationService.getById(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(inMemoryRepository).findById(id);
    }

    @Test
    void create() {
        // Arrange
        LocationCreateDto dto = new LocationCreateDto("location-1", "Location 1");
        ApiLocation location = new ApiLocation(dto.slug(), dto.name());

        when(inMemoryRepository.save(dto.slug(), location)).thenReturn(location);

        // Act
        ApiLocation result = locationService.create(dto);

        // Assert
        assertEquals(location, result);
        verify(inMemoryRepository).save(dto.slug(), location);
    }

    @Test
    void update_existingId() {
        // Arrange
        String id = "location-1";
        LocationUpdateDto dto = new LocationUpdateDto(id, "Updated Location");

        when(inMemoryRepository.update(eq(id), any())).thenReturn(Optional.of(new ApiLocation(id, dto.name())));

        // Act
        ApiLocation result = locationService.update(id, dto);

        // Assert
        assertEquals(dto.name(), result.name());
        verify(inMemoryRepository).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(inMemoryRepository.update(eq(id), any())).thenReturn(Optional.empty());

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationService.update(id, new LocationUpdateDto("location-9l3459", "Some Name"));
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(inMemoryRepository).update(eq(id), any());
    }

    @Test
    void delete_existingId() {
        // Arrange
        String id = "location-1";

        when(inMemoryRepository.deleteById(id)).thenReturn(Optional.of(new ApiLocation(id, "Location 1")));

        // Act
        ApiLocation result = locationService.delete(id);

        // Assert
        assertEquals(id, result.slug());
        verify(inMemoryRepository).deleteById(id);
    }

    @Test
    void delete_nonExistingId() {
        // Arrange
        String id = "location-99";

        when(inMemoryRepository.deleteById(id)).thenReturn(Optional.empty());

        // Act & Assert
        LocationNotExistException exception = assertThrows(LocationNotExistException.class, () -> {
            locationService.delete(id);
        });

        assertEquals("Location with id location-99 does not exist", exception.getMessage());
        verify(inMemoryRepository).deleteById(id);
    }

    @Test
    void restoreLastState_successfully_restored() {
        // Arrange
        String slug = "location-1";
        when(locationHistory.getMemento()).thenReturn(new LocationMemento(slug, "Location 1 last"));

        // Act
        locationService.restoreLastState(slug);

        // Assert
        verify(locationHistory).getMemento();
        verify(inMemoryRepository).update(slug, new ApiLocation(slug, "Location 1 last"));
    }

    @Test
    void restoreLastState_id_not_equals_failed_restored() {
        // Arrange
        String slug = "location-1";
        when(locationHistory.getMemento()).thenReturn(new LocationMemento(slug + "error_in_slug", "Location 1 last"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            locationService.restoreLastState(slug);
        });

        verify(locationHistory).getMemento();
    }

}