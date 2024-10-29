package org.example.task3.list;

import org.example.task3.iterator.CustomLinkedIterator;

public interface CustomLinkedList<T> {

    void add(T element);

    T get(int index);

    T remove(int index);

    boolean contains(Object element);

    void addAll(CustomLinkedList<T> list);

    int size();

    CustomLinkedIterator<T> iterator();

}
