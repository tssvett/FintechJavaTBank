package org.example.task5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.model.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
public class LocationController {
    private final KudaGoServiceClient kudaGoServiceClient;

    @GetMapping
    public List<Location> getLocations() {
        List<Location> locations = kudaGoServiceClient.getLocations();
        log.info("Locations: {}", locations);
        return locations;
    }
}
