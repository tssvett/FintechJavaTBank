package org.example.task5.integration;

import lombok.SneakyThrows;
import org.example.task5.model.Category;
import org.example.task5.model.ApiLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

@Testcontainers
@SpringBootTest
class KudaGoServiceClientIT {

    @Container
    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("place-categories", KudaGoServiceClientIT.class, "categories.json")
            .withMappingFromResource("locations", KudaGoServiceClientIT.class, "locations.json");

    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17")
            .withUsername("postgres")
            .withPassword("123")
            .withDatabaseName("test");

    @SneakyThrows
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private KudaGoServiceClient kudaGoServiceClient;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("rest.kudago-service.host", wireMockContainer::getBaseUrl);
        registry.add("rest.kudago-service.methods.categories.type", () -> "GET");
        registry.add("rest.kudago-service.methods.categories.uri", () -> "/api/categories");
        registry.add("rest.kudago-service.methods.locations.type", () -> "GET");
        registry.add("rest.kudago-service.methods.locations.uri", () -> "/api/locations");

        System.out.println("kudaGoServiceClient url: " + wireMockContainer.getBaseUrl());
    }

    @Test
    void getCategories_shouldReturnListOfCategories() {
        //Act
        List<Category> categoriesFromClient = kudaGoServiceClient.getCategories();

        //Assert
        Assertions.assertEquals(2, categoriesFromClient.size());
    }

    @Test
    void getLocations_shouldReturnListOfLocations() {
        //Act
        List<ApiLocation> locationsFromClient = kudaGoServiceClient.getLocations();

        //Assert
        Assertions.assertEquals(2, locationsFromClient.size());
    }
}