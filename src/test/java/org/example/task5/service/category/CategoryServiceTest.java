package org.example.task5.service.category;

import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.example.task5.utils.generator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private InMemoryRepository<Integer, Category> inMemoryRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private CategoryService categoryService;

    Integer id1;
    Integer id2;
    Category category1;
    Category category2;
    CategoryUpdateDto categoryUpdateDto;
    HashMap<Integer, Category> categories;

    @BeforeEach
    void setUp() {
        id1 = 1;
        id2 = 2;

        category1 = new Category(id1, "category-1", "Category 1");
        category2 = new Category(id2, "category-2", "Category 2");
        categoryUpdateDto = new CategoryUpdateDto("category-1-updated", "Category 1 Updated");

        categories = new HashMap<>();
        categories.put(category1.id(), category1);
        categories.put(category2.id(), category2);
    }

    @Test
    void getAll_notEmpty_returnsList() {
        // Arrange
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
    void getAll_Empty_returnsEmptyList() {
        // Arrange
        when(inMemoryRepository.findAll()).thenReturn(new HashMap<>());

        // Act
        List<Category> result = categoryService.getAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(inMemoryRepository).findAll();
    }

    @Test
    void create_newCategory_returnCategory() {
        // Arrange
        when(idGenerator.generateId()).thenReturn(id1);
        when(inMemoryRepository.save(id1, category1)).thenReturn(category1);

        // Act
        Category result = categoryService.create(new CategoryCreateDto(category1.slug(), category1.name()));

        // Assert
        assertEquals(category1, result);
        verify(idGenerator).generateId();
        verify(inMemoryRepository).save(id1, category1);
    }

    @Test
    void getById_existingId_returnCategory() {
        // Arrange
        when(inMemoryRepository.findById(id1)).thenReturn(Optional.of(category1));

        // Act
        Category result = categoryService.getById(id1);

        // Assert
        assertEquals(category1, result);
        verify(inMemoryRepository).findById(id1);
    }

    @Test
    void getById_notExistingId_throwsCategoryNotExistException() {
        // Arrange
        when(inMemoryRepository.findById(id1)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(CategoryNotExistException.class, () -> categoryService.getById(id1));

        assertEquals("Category with id 1 does not exist", exception.getMessage());
        verify(inMemoryRepository).findById(id1);
    }

    @Test
    void update_existingId_returnCategory() {
        //Arrange
        Category updateCategory = new Category(id1, "category-1-updated", "Category 1 Updated");
        CategoryUpdateDto mappedUpdateCategory = new CategoryUpdateDto("category-1-updated", "Category 1 Updated");

        when(inMemoryRepository.update(id1, updateCategory)).thenReturn(Optional.of(updateCategory));

        //Act
        var updatedCategory = categoryService.update(id1, mappedUpdateCategory);

        //Assert
        assertEquals(updateCategory, updatedCategory);
        assertNotEquals(updatedCategory, category1);
        verify(inMemoryRepository).update(id1, updateCategory);
    }

    @Test
    void update_nonExistingId_throwsCategoryNotExistException() {
        // Arrange
        when(inMemoryRepository.update(eq(id1), any())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(CategoryNotExistException.class, () -> categoryService.update(id1, categoryUpdateDto));

        assertEquals("Category with id 1 does not exist", exception.getMessage());
        verify(inMemoryRepository).update(eq(id1), any());
    }

    @Test
    void delete_existingId_returnCategory() {
        // Arrange
        when(inMemoryRepository.deleteById(id1)).thenReturn(Optional.of(category1));

        // Act
        Category result = categoryService.delete(id1);

        // Assert
        assertEquals(id1, result.id());
        verify(inMemoryRepository).deleteById(id1);
    }

    @Test
    void delete_nonExistingId_throwsCategoryNotExistException() {
        // Arrange
        when(inMemoryRepository.deleteById(id1)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(CategoryNotExistException.class, () -> categoryService.delete(id1));

        assertEquals("Category with id 1 does not exist", exception.getMessage());
        verify(inMemoryRepository).deleteById(id1);
    }
}