package org.example.task5.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.example.task5.properties.DataInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExecutorServiceConfig {
    private final DataInitializationProperties dataInitializationProperties;

    @Bean
    public ExecutorService fixedThreadPool() {
        int poolSize = dataInitializationProperties.getThreads();
        return Executors.newFixedThreadPool(poolSize, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("DataInitializer-Thread" + thread.getId());
            return thread;
        });
    }
}
