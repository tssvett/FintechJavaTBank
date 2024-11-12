package org.example.task11.pattern.mementopattern.catetaker;

import java.util.Deque;
import java.util.LinkedList;
import org.example.task11.pattern.mementopattern.memento.LocationMemento;
import org.springframework.stereotype.Component;

@Component
public class LocationHistory implements Caretaker<LocationMemento> {
    private final Deque<LocationMemento> mementos = new LinkedList<>();

    @Override
    public void addMemento(LocationMemento memento) {
        mementos.add(memento);
    }

    @Override
    public LocationMemento getMemento() {
        return mementos.pop();
    }
}
