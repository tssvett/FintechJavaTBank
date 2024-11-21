package org.example.task11.pattern.mementopattern.catetaker;

import java.util.Deque;
import java.util.LinkedList;
import org.example.task11.pattern.mementopattern.memento.CategoryMemento;
import org.springframework.stereotype.Component;

@Component
public class CategoryHistory implements Caretaker<CategoryMemento> {
    private final Deque<CategoryMemento> mementos = new LinkedList<>();

    @Override
    public void addMemento(CategoryMemento memento) {
        mementos.add(memento);
    }

    @Override
    public CategoryMemento getMemento() {
        return mementos.pop();
    }
}
