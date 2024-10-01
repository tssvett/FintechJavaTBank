package org.example.task5.service.category;

import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Mock
    private InMemoryRepository<Integer, Category> inMemoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        Category category1 = new Category(1, "category-1", "Category 1");
        Category category2 = new Category(2, "category-2", "Category 2");
        HashMap<Integer, Category> categories = new HashMap<>();
        categories.put(category1.id(), category1);
        categories.put(category2.id(), category2);

        when(inMemoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = categoryService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));
        verify(inMemoryRepository).findAll();
    }

    @Test
    void getById_existingId() {
        // Arrange
        Integer id = 1;
        Category category = new Category(id, "category-1", "Category 1");

        when(inMemoryRepository.findById(id)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.getById(id);

        // Assert
        assertEquals(category, result);
        verify(inMemoryRepository).findById(id);
    }

    @Test
    void getById_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(inMemoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryService.getById(id);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(inMemoryRepository).findById(id);
    }

    @Test
    void update_existingId() {
        // Arrange
        Integer id = 1;
        CategoryUpdateDto dto = new CategoryUpdateDto("msk", "Updated Category");

        when(inMemoryRepository.update(eq(id), any())).thenReturn(Optional.of(new Category(id, dto.slug(), dto.name())));

        // Act
        Category result = categoryService.update(id, dto);

        // Assert
        assertEquals(dto.name(), result.name());
        verify(inMemoryRepository).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(inMemoryRepository.update(eq(id), any())).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryService.update(id, new CategoryUpdateDto("Some slug", "Some Name"));
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(inMemoryRepository).update(eq(id), any());
    }

    @Test
    void delete_existingId() {
        // Arrange
        Integer id = 1;

        when(inMemoryRepository.deleteById(id)).thenReturn(Optional.of(new Category(id, "slug","Category 1")));

        // Act
        Category result = categoryService.delete(id);

        // Assert
        assertEquals(id, result.id());
        verify(inMemoryRepository).deleteById(id);
    }

    @Test
    void delete_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(inMemoryRepository.deleteById(id)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryService.delete(id);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(inMemoryRepository).deleteById(id);
    }
}