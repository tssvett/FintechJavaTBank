package org.example.task5.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<V, T> {
    private final ConcurrentHashMap<V, T> storage = new ConcurrentHashMap<>();

    public T save(V id, T entity) {
        return storage.put(id, entity);
    }

    public Optional<T> findById(V id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<T> deleteById(V id) {
        return Optional.ofNullable(storage.remove(id));
    }

    public Map<V, T> findAll() {
        return storage;
    }

    public Optional<T> update(V id, T entity) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            storage.put(id, entity);
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}