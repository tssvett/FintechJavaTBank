package org.example.task5.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.exception.CategoryNotExistException;
import org.example.task5.logtime.annotation.LogExecutionTime;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.example.task5.service.KudaGoService;
import org.example.task5.utils.generator.IdGenerator;
import org.example.task5.utils.mapping.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@LogExecutionTime
public class CategoryService implements KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> {
    private final InMemoryRepository<Integer, Category> inMemoryRepository;
    private final IdGenerator idGenerator;

    @Override
    public List<Category> getAll() {
        return inMemoryRepository.findAll().values().stream().toList();
    }

    @Override
    public Category getById(Integer id) {
        return inMemoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotExistException("Category with id " + id + " does not exist"));
    }

    @Override
    public Category create(CategoryCreateDto categoryCreateDto) {
        int randomId = idGenerator.generateId();
        return inMemoryRepository.save(randomId, Mapper.toCategory(randomId, categoryCreateDto));
    }

    @Override
    public Category update(Integer id, CategoryUpdateDto categoryUpdateDto) {
        return inMemoryRepository.update(id, Mapper.toCategory(id, categoryUpdateDto))
                .orElseThrow(() -> new CategoryNotExistException("Category with id " + id + " does not exist"));
    }

    @Override
    public Category delete(Integer id) {
        return inMemoryRepository.deleteById(id)
                .orElseThrow(() -> new CategoryNotExistException("Category with id " + id + " does not exist"));
    }
}
