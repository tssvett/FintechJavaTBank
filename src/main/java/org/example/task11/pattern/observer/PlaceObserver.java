package org.example.task11.pattern.observer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.repository.PlaceRepository;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PlaceObserver implements Observer {
    private final PlaceRepository placeRepository;

    @Override
    public void update(Observable observable) {
        if (observable instanceof InitializerObservable) {
            try {
                placeRepository.saveAll(((InitializerObservable) observable).getPlacesList());
                log.info("Data successfully initialized with PlaceObserver");
            } catch (Exception e) {
                log.warn("Founded not unique keys, initialization with PlaceObserver failed: {}", e.getMessage());
            }
        }
    }
}
