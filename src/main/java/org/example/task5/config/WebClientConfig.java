package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task5.properties.KudaGoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final KudaGoProperties properties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(properties.getHost())
                .defaultHeader("Accept", "application/json")
                .build();
    }
}