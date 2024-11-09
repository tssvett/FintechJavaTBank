package org.example.task5.config;

import lombok.RequiredArgsConstructor;
import org.example.task5.initializer.Initializer;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class DataInitializerConfig {
    private final Initializer dataInitializer;
    private final ScheduledExecutorService scheduledThreadPool;
    private final Duration dataInitializationPeriod;

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationReady() {
        scheduledThreadPool.scheduleAtFixedRate(dataInitializer::threadingInitializeData, 0,
                dataInitializationPeriod.toSeconds(), TimeUnit.SECONDS);

    }
}
