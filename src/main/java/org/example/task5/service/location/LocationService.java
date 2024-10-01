package org.example.task5.service.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.Location;
import org.example.task5.repository.InMemoryRepository;
import org.example.task5.service.KudaGoService;
import org.example.task5.utils.mapping.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@LogExecutionTime
public class LocationService implements KudaGoService<String, Location, LocationCreateDto, LocationUpdateDto> {
    private final InMemoryRepository<String, Location> inMemoryRepository;

    @Override
    public List<Location> getAll() {
        return inMemoryRepository.findAll().values().stream().toList();
    }

    @Override
    public Location getById(String id) {
        return inMemoryRepository.findById(id)
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }

    @Override
    public Location create(LocationCreateDto locationCreateDto) {
        return inMemoryRepository.save(locationCreateDto.slug(), Mapper.toLocation(locationCreateDto)); // Предполагаем, что slug уникален
    }

    @Override
    public Location update(String id, LocationUpdateDto locationUpdateDto) {
        return inMemoryRepository.update(id, Mapper.toLocation(locationUpdateDto))
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }

    @Override
    public Location delete(String id) {
        return inMemoryRepository.deleteById(id)
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }
}