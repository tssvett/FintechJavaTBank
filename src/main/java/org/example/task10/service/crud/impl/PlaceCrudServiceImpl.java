package org.example.task10.service.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.enitiy.Place;
import org.example.task10.exception.EntityDeleteException;
import org.example.task10.exception.PlaceNotFoundException;
import org.example.task10.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceCrudServiceImpl {
    private final PlaceRepository placeRepository;

    public Place read(UUID id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("Event not found with id: " + id));
    }

    public void delete(UUID id) {
        try {
            placeRepository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeleteException("Failed to delete place with id: " + id);
        }
    }
}
