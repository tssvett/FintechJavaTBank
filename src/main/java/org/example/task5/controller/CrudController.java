package org.example.task5.controller;

import java.util.List;

public interface CrudController<I, T, C, U> {

    List<T> getAll();

    T getById(I id);

    T create(C createDto);

    T update(I id, U updateDto);

    void delete(I id);
}
