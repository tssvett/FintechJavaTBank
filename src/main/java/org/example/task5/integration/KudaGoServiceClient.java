package org.example.task5.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.model.Category;
import org.example.task5.model.Location;
import org.example.task5.properties.KudaGoProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KudaGoServiceClient {
    private final WebClient webClient;
    private final KudaGoProperties kudaGoProperties;

    public List<Category> getCategories() {
        return webClient
                .get()
                .uri(kudaGoProperties.getMethods().getCategories().getUri())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Category>>() {
                })
                .block();
    }

    public List<Location> getLocations() {
        return webClient
                .get()
                .uri(kudaGoProperties.getMethods().getLocations().getUri())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Location>>() {
                })
                .block();
    }
}