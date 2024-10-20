package org.example.task5.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.controller.CrudController;
import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.model.Category;
import org.example.task5.service.KudaGoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/places/categories")
@RequiredArgsConstructor
public class CategoryCrudController implements CrudController<Integer, Category, CategoryCreateDto, CategoryUpdateDto> {
    private final KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> categoryService;

    @Override

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @Override

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category getById(@PathVariable("id") Integer id) {
        return categoryService.getById(id);
    }

    @Override

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody CategoryCreateDto categoryCreateDto) {
        return categoryService.create(categoryCreateDto);
    }

    @Override

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category update(@PathVariable("id") Integer id, @RequestBody CategoryUpdateDto categoryUpdateDto) {
        return categoryService.update(id, categoryUpdateDto);
    }

    @Override

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        categoryService.delete(id);
    }
}
