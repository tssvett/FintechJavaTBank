package org.example.task5.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.RequiredArgsConstructor;
import org.example.task5.properties.DataInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
