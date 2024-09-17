package org.example.list;

import java.util.List;

public interface CustomLinkedList<T> {

    void add(T element);

    T get(int index);

    T remove(int index);

    boolean contains(Object element);

    void addAll(CustomLinkedList<T> list);

    int size();

}
