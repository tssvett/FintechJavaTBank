package org.example.task5.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "data-initialization")
public class DataInitializationProperties {
    private int threads;
    private int periodSeconds;
}
