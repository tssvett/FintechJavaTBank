package org.example.task5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.integration.KudaGoServiceClient;
import org.example.task5.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/places/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final KudaGoServiceClient kudaGoServiceClient;

    @GetMapping
    public List<Category> getCategories() {
        List<Category> categories = kudaGoServiceClient.getCategories();
        log.info("Categories: {}", categories);
        return categories;
    }
}
