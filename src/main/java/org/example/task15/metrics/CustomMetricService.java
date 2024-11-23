package org.example.task15.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricService {
    private final Counter requestCounter;

    public CustomMetricService(MeterRegistry registry) {
        requestCounter = registry.counter("custom_counter");
    }

    public void incrementRequestCount() {
        requestCounter.increment();
    }
}
