package org.example.task3.iterator;

import java.util.function.Consumer;

public interface CustomLinkedIterator<E> {
    boolean hasNext();

    E next();

    void forEachRemaining(Consumer<? super E> action);
}
