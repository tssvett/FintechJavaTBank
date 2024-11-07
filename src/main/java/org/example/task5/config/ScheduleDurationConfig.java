package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task5.properties.DataInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ScheduleDurationConfig {
    private final DataInitializationProperties dataInitializationProperties;

    @Bean
    public Duration dataInitializationPeriod() {
        return Duration.ofSeconds(dataInitializationProperties.getPeriodSeconds());
    }
}
