package org.example.task5.controller.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.model.Location;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KudaGoService<String, Location, LocationCreateDto, LocationUpdateDto> locationService;

    private Location location;

    @BeforeEach
    void setUp() {
        // Initialize a Location object with slug and name
        location = new Location("test-location", "Test Location");
    }

    @Test
    void getAllLocations_shouldReturnAllLocations() throws Exception {
        when(locationService.getAll()).thenReturn(List.of(location));

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].slug").value(location.slug()))
                .andExpect(jsonPath("$[0].name").value(location.name()));

        verify(locationService).getAll();
    }

    @Test
    void getLocationBySlug_shouldReturnLocation_whenLocationExists() throws Exception {
        String slug = "test-location";
        when(locationService.getById(slug)).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/{slug}", slug))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.slug").value(location.slug()))
                .andExpect(jsonPath("$.name").value(location.name()));

        verify(locationService).getById(slug);
    }

    @Test
    void getLocationBySlug_shouldReturnNotFound_whenLocationDoesNotExist() throws Exception {
        String slug = "non-existent-location"; // Assuming this slug does not exist
        when(locationService.getById(slug)).thenThrow(new LocationNotExistException("Location not found"));

        mockMvc.perform(get("/api/v1/locations/{id}", slug))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("LocationNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Location not found"));

        verify(locationService).getById(slug);
    }

    @Test
    void createANewLocation_shouldCreateLocation() throws Exception {
        LocationCreateDto createDto = new LocationCreateDto("new-location", "New Location");
        Location createdLocation = new Location(createDto.slug(), createDto.name());

        when(locationService.create(any(LocationCreateDto.class))).thenReturn(createdLocation);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.slug").value(createdLocation.slug()))
                .andExpect(jsonPath("$.name").value(createdLocation.name()));

        verify(locationService).create(createDto);
    }

    @Test
    void updateAnExistingLocation_shouldUpdateLocation() throws Exception {
        String slug = "test-location";
        LocationUpdateDto updateDto = new LocationUpdateDto("updated-location", "Updated Name");

        when(locationService.update(eq(slug), any(LocationUpdateDto.class)))
                .thenReturn(new Location(updateDto.slug(), updateDto.name()));

        mockMvc.perform(put("/api/v1/locations/{id}", slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.slug").value(updateDto.slug()))
                .andExpect(jsonPath("$.name").value(updateDto.name()));

        verify(locationService).update(eq(slug), any(LocationUpdateDto.class));
    }

    @Test
    void updateAnExistingLocation_shouldReturnNotFound_whenLocationDoesNotExist() throws Exception {
        String slug = "non-existent-location"; // Assuming this slug does not exist
        LocationUpdateDto updateDto = new LocationUpdateDto("updated-location", "Updated Name");

        when(locationService.update(eq(slug), any(LocationUpdateDto.class)))
                .thenThrow(new LocationNotExistException("Location not found"));

        mockMvc.perform(put("/api/v1/locations/{id}", slug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("LocationNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Location not found"));

        verify(locationService).update(eq(slug), any(LocationUpdateDto.class));
    }

    @Test
    void deleteALocationBySlug_shouldDeleteLocation() throws Exception {
        String slug = "test-location";

        mockMvc.perform(delete("/api/v1/locations/{id}", slug))
                .andExpect(status().isNoContent());

        verify(locationService).delete(slug);
    }

    @Test
    void deleteALocationBySlug_shouldReturnNotFound_whenLocationDoesNotExist() throws Exception {
        String slug = "non-existent-location";
        doThrow(new LocationNotExistException("Location not found")).when(locationService).delete(slug);

        mockMvc.perform(delete("/api/v1/locations/{slug}", slug))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("LocationNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Location not found"));

        verify(locationService).delete(slug);
    }
}