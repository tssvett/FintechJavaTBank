package org.example.task5.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.exception.KudaGoException;
import org.example.task5.model.Category;
import org.example.task5.model.ApiLocation;
import org.example.task5.properties.KudaGoProperties;
import org.example.task9.dto.EventResponse;
import org.example.task9.model.ApiEvent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new KudaGoException("Failed to fetch categories: " + clientResponse.statusCode()));
                })
                .bodyToMono(new ParameterizedTypeReference<List<Category>>() {
                })
                .doOnNext(categories -> log.info("Successfully fetched {} categories", categories.size()))
                .doOnError(e -> log.error("Error fetching categories: {}", e.getMessage()))
                .block();
    }

    public List<ApiLocation> getLocations() {
        return webClient
                .get()
                .uri(kudaGoProperties.getMethods().getLocations().getUri())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new KudaGoException("Failed to fetch locations: " + clientResponse.statusCode()));
                })
                .bodyToMono(new ParameterizedTypeReference<List<ApiLocation>>() {
                })
                .doOnNext(locations -> log.info("Successfully fetched {} locations", locations.size()))
                .doOnError(e -> log.error("Error fetching locations: {}", e.getMessage()))
                .block();
    }

    public CompletableFuture<List<ApiEvent>> getEventsFuture(LocalDate dateFrom, LocalDate dateTo, String location) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(kudaGoProperties.getMethods().getEvents().getUri())
                        .queryParam("actual_since", dateFrom.toString())
                        .queryParam("actual_until", dateTo.toString())
                        .queryParam("order_by", "is_free,price")
                        .queryParam("text_format", "text")
                        .queryParam("location", location)
                        .queryParam("fields", "id,title,price,is_free,dates,place")
                        .queryParam("expand", "place")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new KudaGoException("Failed to fetch events: " + clientResponse.statusCode()));
                })
                .bodyToMono(EventResponse.class)
                .map(EventResponse::results)
                .doOnNext(events -> log.info("Successfully fetched {} events", events.size()))
                .toFuture();
    }

    public Mono<List<ApiEvent>> getEventsReactive(LocalDate dateFrom, LocalDate dateTo) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(kudaGoProperties.getMethods().getEvents().getUri())
                        .queryParam("actual_since", dateFrom.toString())
                        .queryParam("actual_until", dateTo.toString())
                        .queryParam("order_by", "is_free,price")
                        .queryParam("text_format", "text")
                        .queryParam("location", "smr")
                        .queryParam("fields", "id,title,price,is_free,dates,place")
                        .queryParam("expand", "place")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new KudaGoException("Failed to fetch events: " + clientResponse.statusCode()));
                })
                .bodyToMono(EventResponse.class)
                .map(EventResponse::results);
    }
}