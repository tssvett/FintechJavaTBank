package org.example.task5.logtime.LogExecutionTimeAutoConfiguration;

import org.example.task5.logtime.postbeanprocessor.LogExecutionTimePostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogExecutionTimeAutoConfiguration {

    @Bean
    public LogExecutionTimePostProcessor logExecutionTimePostProcessor() {
        return new LogExecutionTimePostProcessor();
    }
}