package org.example.task5.model;

import org.example.task11.pattern.mementopattern.memento.CategoryMemento;
import org.example.task11.pattern.mementopattern.originator.Originator;

public record Category(
        int id,
        String slug,
        String name
) implements Originator<CategoryMemento, Category> {

    @Override
    public CategoryMemento saveState() {
        return new CategoryMemento(id, slug, name);
    }

    @Override
    public Category restoreState(CategoryMemento memento) {
        return new Category(memento.id(), memento.slug(), memento.name());
    }
}
