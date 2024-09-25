package org.example.task5.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRepositoryTest {

    private InMemoryRepository<Integer, String> repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository<>();
    }

    @Test
    void save_and_findById() {
        // Arrange
        Integer id = 1;
        String entity = "Test Entity";

        // Act
        repository.save(id, entity);

        // Assert
        Optional<String> result = repository.findById(id);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void findById_nonExistingId() {
        // Arrange
        Integer id = 99;

        // Act
        Optional<String> result = repository.findById(id);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void update_existingId() {
        // Arrange
        Integer id = 1;
        String initialEntity = "Initial Entity";
        String updatedEntity = "Updated Entity";

        repository.save(id, initialEntity);

        // Act
        Optional<String> result = repository.update(id, updatedEntity);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(updatedEntity, result.get());

        // Verify the updated value
        Optional<String> updatedResult = repository.findById(id);
        assertTrue(updatedResult.isPresent());
        assertEquals(updatedEntity, updatedResult.get());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        Integer id = 99;
        String updatedEntity = "Updated Entity";

        // Act
        Optional<String> result = repository.update(id, updatedEntity);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void delete_existingId() {
        // Arrange
        Integer id = 1;
        String entity = "Test Entity";

        repository.save(id, entity);

        // Act
        Optional<String> deletedEntity = repository.deleteById(id);

        // Assert
        assertTrue(deletedEntity.isPresent());
        assertEquals(entity, deletedEntity.get());

        // Verify that the entity is no longer present
        Optional<String> resultAfterDelete = repository.findById(id);
        assertFalse(resultAfterDelete.isPresent());
    }

    @Test
    void delete_nonExistingId() {
        // Arrange
        Integer id = 99;

        // Act
        Optional<String> deletedEntity = repository.deleteById(id);

        // Assert
        assertFalse(deletedEntity.isPresent());
    }

    @Test
    void findAll() {
        // Arrange
        Integer id1 = 1;
        Integer id2 = 2;
        String entity1 = "Entity 1";
        String entity2 = "Entity 2";

        repository.save(id1, entity1);
        repository.save(id2, entity2);

        // Act
        Map<Integer, String> allEntities = repository.findAll();

        // Assert
        assertEquals(2, allEntities.size());
        assertEquals(entity1, allEntities.get(id1));
        assertEquals(entity2, allEntities.get(id2));
    }
}