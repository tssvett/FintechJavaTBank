package org.example.task5.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    public Optional<List<Category>> findAllCategories() {
        //TODO
    }
}

    public List<Category> getAllCategories() {
        //TODO
    }

    public Optional<Category> findCategoryById(int id) {
        //TODO
    }

    public Category getCategoryById(int id) {
        //TODO
    }

    public Category createCategory(CategoryCreateDto category) {
        //TODO
    }

    public Category updateCategory(int id, CategoryUpdateDto category) {
        //TODO
    }

    public void deleteCategory(int id) {
        //TODO
    }
}
