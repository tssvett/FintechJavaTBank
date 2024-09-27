package org.example.task5.controller.category;

import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.model.Category;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> categoryService;

    @InjectMocks
    private CategoryController categoryController;


    Category category1;
    CategoryCreateDto categoryCreateDto1;
    CategoryUpdateDto categoryUpdateDto1;
    Category category2;
    CategoryCreateDto categoryCreateDto2;
    CategoryUpdateDto categoryUpdateDto2;

    @BeforeEach
    void before() {
        category1 = new Category(1, "category-1", "Category 1");
        categoryCreateDto1 = new CategoryCreateDto("category-1", "Category 1");
        categoryUpdateDto1 = new CategoryUpdateDto("updatedCategory-1", "updatedCategory 1");

        category2 = new Category(2, "category-2", "Category 2");
        categoryCreateDto2 = new CategoryCreateDto("category-2", "Category 2");
        categoryUpdateDto2 = new CategoryUpdateDto("updatedCategory-2", "updatedCategory 2");
    }

    @Test
    void getAll_notEmptyDatasource_shouldReturnNotEmptyList() {
        // Arrange
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
    void getAll_emptyDatasource_shouldReturnEmptyList() {
        // Arrange
        when(categoryService.getAll()).thenReturn(List.of());

        // Act
        List<Category> result = categoryController.getAll();

        // Assert
        assertEquals(0, result.size());
        verify(categoryService).getAll();
    }

    @Test
    void getById_existingId_shouldReturnCategory() {
        // Arrange
        int id = 1;
        when(categoryService.getById(id)).thenReturn(category1);

        // Act
        Category result = categoryController.getById(id);

        // Assert
        assertEquals(category1, result);
        verify(categoryService).getById(id);
    }

    @Test
    void getById_notExistingId_shouldThrowException() {
        // Arrange
        Integer id = 99;

        when(categoryService.getById(id)).thenThrow(new CategoryNotExistException("Category with id " + id + " does not exist"));

        // Act & Assert
        var exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryController.getById(id);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(categoryService).getById(id);
    }

    @Test
    void create_shouldReturnCreatedCategory() {
        // Arrange
        when(categoryService.create(categoryCreateDto1)).thenReturn(category1);

        // Act
        Category result = categoryController.create(categoryCreateDto1);

        // Assert
        assertEquals(categoryCreateDto1.name(), result.name());
        assertEquals(categoryCreateDto1.slug(), result.slug());
        verify(categoryService).create(categoryCreateDto1);
    }

    @Test
    void update_existingId() {
        // Arrange
        Integer id = 1;

        when(categoryService.update(id, categoryUpdateDto1)).thenReturn(new Category(id, categoryUpdateDto1.slug(), categoryUpdateDto1.name()));

        // Act
        Category result = categoryController.update(id, categoryUpdateDto1);

        // Assert
        assertEquals(categoryUpdateDto1.name(), result.name());
        verify(categoryService).update(eq(id), any());
    }

    @Test
    void update_nonExistingId() {
        // Arrange
        Integer id = 99;

        when(categoryService.update(eq(id), any())).thenThrow(new CategoryNotExistException("Category with id " + id + " does not exist"));

        // Act & Assert
        CategoryNotExistException exception = assertThrows(CategoryNotExistException.class, () -> {
            categoryController.update(id, categoryUpdateDto1);
        });

        assertEquals("Category with id 99 does not exist", exception.getMessage());
        verify(categoryService).update(eq(id), any());
    }

    @Test
    void delete_existingId() {
        // Arrange
        Integer id = 1;

        when(categoryService.delete(id)).thenReturn(category1);

        // Act
        categoryController.delete(id);

        // Assert
        verify(categoryService).delete(id);
        verifyNoMoreInteractions(categoryService);
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