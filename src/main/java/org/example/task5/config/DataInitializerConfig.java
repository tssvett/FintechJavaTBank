package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task11.pattern.command.handler.CommandInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "data-initialization.enable", havingValue = "true")
public class DataInitializerConfig {
    private final CommandInvoker commandInvoker;


    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationReady() {
        commandInvoker.initializeDatabase();
    }
}
