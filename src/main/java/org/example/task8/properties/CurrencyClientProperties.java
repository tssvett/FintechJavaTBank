package org.example.task8.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rest.currency-rate-service")
public class CurrencyClientProperties {
    private String host;
    private Methods methods;

    @Getter
    @Setter
    public static class Methods {
        private Method rates;

        @Getter
        @Setter
        public static class Method {
            private String type;
            private String uri;
        }
    }
}
