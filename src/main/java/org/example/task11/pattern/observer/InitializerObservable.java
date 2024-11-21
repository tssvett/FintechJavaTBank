package org.example.task11.pattern.observer;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.entity.Place;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
@Getter
@RequiredArgsConstructor
public class InitializerObservable implements Observable {
    private final List<Observer> observers = new ArrayList<>();
    private List<Place> placesList;

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
