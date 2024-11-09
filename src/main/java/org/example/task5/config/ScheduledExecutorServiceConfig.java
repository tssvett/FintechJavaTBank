package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task5.properties.DataInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
public class ScheduledExecutorServiceConfig {
    private final DataInitializationProperties dataInitializationProperties;

    @Bean
    public ScheduledExecutorService scheduledThreadPool() {
        int poolSize = dataInitializationProperties.getThreads();
        return Executors.newScheduledThreadPool(poolSize);
    }
}
