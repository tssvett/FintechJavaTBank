package org.example.task12.properties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtProperties {
    private String jwtKey;
    private @DurationUnit(ChronoUnit.SECONDS) Duration defaultExpiration;
    private @DurationUnit(ChronoUnit.SECONDS) Duration rememberMeExpiration;
}
