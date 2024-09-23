package org.example.task5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.model.Location;
import org.example.task5.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Location> getLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Location getLocationById(@PathVariable int id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Location createLocation(@RequestBody LocationCreateDto location) {
        return locationService.createLocation(location);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Location updateLocation(@PathVariable int id, @RequestBody LocationUpdateDto location) {
        return locationService.updateLocation(id, location);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable int id) {
        locationService.deleteLocation(id);
    }
}
