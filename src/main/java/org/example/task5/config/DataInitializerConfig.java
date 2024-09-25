package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task5.initializer.Initializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class DataInitializerConfig {
    private final Initializer dataInitializer;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationReady() {
        dataInitializer.initializeData();
    }
}
