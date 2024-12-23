package org.example.task5.config;

import org.example.task5.model.ApiLocation;
import org.example.task5.model.Category;
import org.example.task5.repository.InMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public InMemoryRepository<Integer, Category> categoryRepository() {
        return new InMemoryRepository<>();
    }

    @Bean
    public InMemoryRepository<String, ApiLocation> locationRepository() {
        return new InMemoryRepository<>();
    }
}
