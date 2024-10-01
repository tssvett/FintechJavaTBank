package org.example.task5.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.model.Category;
import org.example.task5.service.KudaGoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1, "test-category", "Test Category");
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(category.id()))
                .andExpect(jsonPath("$[0].slug").value(category.slug()))
                .andExpect(jsonPath("$[0].name").value(category.name()));

        verify(categoryService).getAll();
    }

    @Test
    void getCategoryById_shouldReturnCategory_whenCategoryExists() throws Exception {
        int id = 1;
        when(categoryService.getById(id)).thenReturn(category);

        mockMvc.perform(get("/api/v1/places/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.id()))
                .andExpect(jsonPath("$.slug").value(category.slug()))
                .andExpect(jsonPath("$.name").value(category.name()));

        verify(categoryService).getById(id);
    }

    @Test
    void getCategoryById_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        int id = 99;
        when(categoryService.getById(id)).thenThrow(new CategoryNotExistException("Category not found"));

        mockMvc.perform(get("/api/v1/places/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("CategoryNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Category not found"));

        verify(categoryService).getById(id);
    }

    @Test
    void createANewCategory_shouldCreateCategory() throws Exception {
        CategoryCreateDto createDto = new CategoryCreateDto("new-category", "New Category");
        Category createdCategory = new Category(2, createDto.slug(), createDto.name());

        when(categoryService.create(any(CategoryCreateDto.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdCategory.id()))
                .andExpect(jsonPath("$.slug").value(createdCategory.slug()))
                .andExpect(jsonPath("$.name").value(createdCategory.name()));

        verify(categoryService).create(createDto);
    }

    @Test
    void updateAnExistingCategory_shouldUpdateCategory() throws Exception {
        int id = 1;
        CategoryUpdateDto updateDto = new CategoryUpdateDto("updated-category", "Updated Category");

        when(categoryService.update(eq(id), any(CategoryUpdateDto.class)))
                .thenReturn(new Category(id, updateDto.slug(), updateDto.name()));

        mockMvc.perform(put("/api/v1/places/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.slug").value(updateDto.slug()))
                .andExpect(jsonPath("$.name").value(updateDto.name()));

        verify(categoryService).update(eq(id), any(CategoryUpdateDto.class));
    }

    @Test
    void updateAnExistingCategory_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        int id = 99;
        CategoryUpdateDto updateDto = new CategoryUpdateDto("updated-category", "Updated Category");

        when(categoryService.update(eq(id), any(CategoryUpdateDto.class)))
                .thenThrow(new CategoryNotExistException("Category not found"));

        mockMvc.perform(put("/api/v1/places/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("CategoryNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Category not found"));

        verify(categoryService).update(eq(id), any(CategoryUpdateDto.class));
    }

    @Test
    void deleteACategoryById_shouldDeleteCategory() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/api/v1/places/categories/{id}", id))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(id);
    }

    @Test
    void deleteACategoryById_shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
        int id = 99;
        doThrow(new CategoryNotExistException("Category not found")).when(categoryService).delete(id);

        mockMvc.perform(delete("/api/v1/places/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionName").value("CategoryNotExistException"))
                .andExpect(jsonPath("$.exceptionMessage").value("Category not found"));

        verify(categoryService).delete(id);
    }
}