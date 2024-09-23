package org.example.task5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    public Optional<List<Location>> findAllLocations() {
        //TODO
    }

}

    public List<Location> getAllLocations() {
        //TODO
    }

    public Optional<Location> findLocationById(int id) {
        //TODO
    }

    public Location getLocationById(int id) {
        //TODO
    }

    public Location createLocation(LocationCreateDto location) {
        //TODO
    }

    public Location updateLocation(int id, LocationUpdateDto location) {
        //TODO
    }

    public void deleteLocation(int id) {
        //TODO
    }
}
