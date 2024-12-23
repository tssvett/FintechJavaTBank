package org.example.task8.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.exception.ServiceUnavailableException;
import org.example.task8.integration.circuitbreaker.CurrencyRateFallback;
import org.example.task8.parser.Parser;
import org.example.task8.parser.xml.model.Valute;
import org.example.task8.properties.CurrencyClientProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateServiceClient {
    private final Parser<List<Valute>> xmlParser;
    private final WebClient currencyWebClient;
    private final CurrencyClientProperties currencyClientProperties;
    private final CurrencyRateFallback currencyRateFallback;

    @CircuitBreaker(name = "currencyRateServiceClient", fallbackMethod = "currencyRateFallback.handleFallback")
    @Cacheable(value = "currencyCache", key = "#root.method.name", unless = "#result == null")
    public List<Valute> getCurrencyRates() {
        return currencyWebClient
                .get()
                .uri(currencyClientProperties.getMethods().getRates().getUri())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new ServiceUnavailableException("Failed to fetch currencies: "
                            + clientResponse.statusCode()));
                })
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .doOnNext(valutesList -> log.info("Successfully fetched {} currencies", valutesList.size()))
                .doOnError(e -> log.error("Error fetching currencies: {}", e.getMessage()))
                .block();
    }

    @CircuitBreaker(name = "currencyRateServiceClient", fallbackMethod = "currencyRateFallback.handleFallback")
    public CompletableFuture<List<Valute>> getCurrencyRatesFuture() {
        return currencyWebClient
                .get()
                .uri(currencyClientProperties.getMethods().getRates().getUri())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new ServiceUnavailableException("Failed to fetch currencies: "
                            + clientResponse.statusCode()));
                })
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .doOnNext(valutesList -> log.info("Successfully fetched {} currencies", valutesList.size()))
                .doOnError(e -> log.error("Error fetching currencies: {}", e.getMessage()))
                .toFuture();
    }

    @CircuitBreaker(name = "currencyRateServiceClient", fallbackMethod = "currencyRateFallback.handleFallback")
    public Mono<List<Valute>> getCurrencyRatesReactive() {
        return currencyWebClient
                .get()
                .uri(currencyClientProperties.getMethods().getRates().getUri())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new ServiceUnavailableException("Failed to fetch currencies: "
                            + clientResponse.statusCode()));
                })
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .doOnNext(valutesList -> log.info("Successfully fetched {} currencies", valutesList.size()))
                .doOnError(e -> log.error("Error fetching currencies: {}", e.getMessage()));
    }
}
