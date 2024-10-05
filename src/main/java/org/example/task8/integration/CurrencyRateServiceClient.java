package org.example.task8.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.exception.KudaGoException;
import org.example.task8.parser.Parser;
import org.example.task8.parser.xml.model.Valute;
import org.example.task8.properties.CurrencyClientProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateServiceClient {
    private final Parser<List<Valute>> xmlParser;
    private final WebClient webClient;
    private final CurrencyClientProperties currencyClientProperties;

    public List<Valute> getCurrencyRates() {
        return webClient
                .get()
                .uri(currencyClientProperties.getMethods().getRates().getUri())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error response: {}", clientResponse.statusCode());
                    return Mono.error(new KudaGoException("Failed to fetch currencies: " + clientResponse.statusCode()));
                })
                .bodyToMono(String.class)
                .map(xmlParser::parse)
                .doOnNext(valutesList -> log.info("Successfully fetched {} currencies", valutesList.size()))
                .doOnError(e -> log.error("Error fetching currencies: {}", e.getMessage()))
                .block();
    }
}
