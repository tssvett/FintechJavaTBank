package org.example.task11.pattern.mementopattern.originator;

public interface Originator<E, R> {

    E saveState();

    R restoreState(E memento);
}
