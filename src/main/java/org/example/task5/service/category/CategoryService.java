package org.example.task5.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task11.pattern.mementopattern.catetaker.CategoryHistory;
import org.example.task11.pattern.mementopattern.memento.CategoryMemento;
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

@Slf4j
@Service
@RequiredArgsConstructor
@LogExecutionTime
public class CategoryService implements KudaGoService<Integer, Category, CategoryCreateDto, CategoryUpdateDto> {
    private final InMemoryRepository<Integer, Category> inMemoryRepository;
    private final IdGenerator idGenerator;
    private final CategoryHistory categoryHistory;

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
        Category category = this.getById(id);
        categoryHistory.addMemento(new CategoryMemento(category.id(), category.slug(), category.name()));

        return inMemoryRepository.update(id, Mapper.toCategory(id, categoryUpdateDto))
                .orElseThrow(() -> new CategoryNotExistException("Category with id " + id + " does not exist"));
    }

    @Override
    public Category delete(Integer id) {
        Category category = this.getById(id);
        categoryHistory.addMemento(new CategoryMemento(category.id(), category.slug(), category.name()));

        return inMemoryRepository.deleteById(id)
                .orElseThrow(() -> new CategoryNotExistException("Category with id " + id + " does not exist"));
    }

    public void restoreLastState(Integer id) {
        CategoryMemento memento = categoryHistory.getMemento();
        if (memento.id() != id) {
            throw new IllegalStateException("Last memento is not for category with id " + id);
        }

        Category category = new Category(memento.id(), memento.slug(), memento.name());
        inMemoryRepository.update(id, category);
    }
}
