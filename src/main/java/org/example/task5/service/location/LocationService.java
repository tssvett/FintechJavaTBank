package org.example.task5.service.location;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task11.pattern.mementopattern.catetaker.LocationHistory;
import org.example.task11.pattern.mementopattern.memento.LocationMemento;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.exception.LocationNotExistException;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.ApiLocation;
import org.example.task5.repository.InMemoryRepository;
import org.example.task5.service.KudaGoService;
import org.example.task5.utils.mapping.Mapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@LogExecutionTime
public class LocationService implements KudaGoService<String, ApiLocation, LocationCreateDto, LocationUpdateDto> {
    private final InMemoryRepository<String, ApiLocation> inMemoryRepository;
    private final LocationHistory locationHistory;

    @Override
    public List<ApiLocation> getAll() {
        return inMemoryRepository.findAll().values().stream().toList();
    }

    @Override
    public ApiLocation getById(String id) {
        return inMemoryRepository.findById(id)
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }

    @Override
    public ApiLocation create(LocationCreateDto locationCreateDto) {
        return inMemoryRepository.save(locationCreateDto.slug(),
                Mapper.toLocation(locationCreateDto));
    }

    @Override
    public ApiLocation update(String id, LocationUpdateDto locationUpdateDto) {
        ApiLocation location = this.getById(id);
        locationHistory.addMemento(new LocationMemento(location.slug(), location.name()));

        return inMemoryRepository.update(id, Mapper.toLocation(locationUpdateDto))
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }

    @Override
    public ApiLocation delete(String id) {
        ApiLocation location = this.getById(id);
        locationHistory.addMemento(new LocationMemento(location.slug(), location.name()));

        return inMemoryRepository.deleteById(id)
                .orElseThrow(() -> new LocationNotExistException("Location with id " + id + " does not exist"));
    }

    public void restoreLastState(String slug) {
        LocationMemento memento = locationHistory.getMemento();
        if (!memento.slug().equals(slug)) {
            throw new IllegalStateException("Last memento is not for category with slug " + slug);
        }

        ApiLocation location = new ApiLocation(memento.slug(), memento.name());
        inMemoryRepository.update(slug, location);
    }
}