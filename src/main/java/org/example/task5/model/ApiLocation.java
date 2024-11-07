package org.example.task5.model;

import org.example.task11.pattern.mementopattern.memento.LocationMemento;
import org.example.task11.pattern.mementopattern.originator.Originator;

public record ApiLocation(
        String slug,
        String name
) implements Originator<LocationMemento, ApiLocation> {

    @Override
    public LocationMemento saveState() {
        return new LocationMemento(slug, name);
    }

    @Override
    public ApiLocation restoreState(LocationMemento memento) {
        return new ApiLocation(memento.slug(), memento.name());
    }
}
