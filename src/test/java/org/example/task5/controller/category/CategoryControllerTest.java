package org.example.task5.controller.category;

import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.model.Category;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    @Mock
    private KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        Category category1 = new Category(1, "category-1", "Category 1");
        Category category2 = new Category(2, "category-2", "Category 2");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAll()).thenReturn(categories);

        // Act
        List<Category> result = categoryController.getAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));
        verify(categoryService).getAll();
    }

    @Test
    void getById_existingId() {
        // Arrange
        Integer id = 1;
        Category category = new Category(id, "category-1", "Category 1");

        when(categoryService.getById(id)).thenReturn(category);

        // Act
        Category result = categoryController.getById(id);

        // Assert
        assertEquals(category, result);
        verify(categoryService).getById(id);
    }

    @Test
    void getById_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(categoryService.getById(id)).thenThrow(new CategoryNotExistException("Category with id " + id + " does not exist"));

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryController.getById(id);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(categoryService).getById(id);
    }

    @Test
    void create() {
        // Arrange
        CategoryCreateDto dto = new CategoryCreateDto("category-1", "Category 1");
        Category createdCategory = new Category(1, dto.slug(), dto.name());

        when(categoryService.create(dto)).thenReturn(createdCategory);

        // Act
        Category result = categoryController.create(dto);

        // Assert
        assertEquals(createdCategory, result);
        verify(categoryService).create(dto);
    }

    @Test
    void update_existingId() {
        // Arrange
        Integer id = 1;
        CategoryUpdateDto dto = new CategoryUpdateDto("slug", "Updated Category");

        when(categoryService.update(eq(id), any())).thenReturn(new Category(id, "category-1", dto.name()));

        // Act
        Category result = categoryController.update(id, dto);

        // Assert
        assertEquals(dto.name(), result.name());
        verify(categoryService).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(categoryService.update(eq(id), any())).thenThrow(new CategoryNotExistException("Category with id " + id + " does not exist"));

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryController.update(id, new CategoryUpdateDto("slug", "Some Name"));
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(categoryService).update(eq(id), any());
    }


    @Test
    void delete_nonExistingId() {
        // Arrange
        Integer id = 99;

        doThrow(new CategoryNotExistException("Category with id " + id + " does not exist")).when(categoryService).delete(id);

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryController.delete(id);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(categoryService).delete(id);
    }
}