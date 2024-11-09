package org.example.task8.config;

import lombok.RequiredArgsConstructor;
import org.example.task8.properties.CurrencyClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientCurrencyConfig {
    private final CurrencyClientProperties properties;

    @Bean
    public WebClient currencyWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getHost())
                .build();
    }
}
