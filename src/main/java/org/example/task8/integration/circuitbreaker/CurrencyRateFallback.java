package org.example.task8.integration.circuitbreaker;

import lombok.extern.slf4j.Slf4j;
import org.example.task8.exception.ServiceUnavailableException;
import org.example.task8.parser.xml.model.Valute;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CurrencyRateFallback {

    public List<Valute> handleFallback(Throwable throwable) {
        log.error("Circuit breaker is active! Something gone wrong");
        throw new ServiceUnavailableException("Service is unavailable");
    }
}
