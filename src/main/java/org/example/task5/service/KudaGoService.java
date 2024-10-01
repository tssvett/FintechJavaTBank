package org.example.task5.service;

import java.util.List;

public interface KudaGoService<I, T, C, U> {
    List<T> getAll();

    T getById(I id);

    T create(C createDto);

    T update(I id, U updateDto);

    T delete(I id);
}
