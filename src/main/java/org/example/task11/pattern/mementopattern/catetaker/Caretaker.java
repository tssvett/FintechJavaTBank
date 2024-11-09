package org.example.task11.pattern.mementopattern.catetaker;

public interface Caretaker<E> {

    void addMemento(E memento);

    E getMemento();
}
