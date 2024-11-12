package org.example.task5.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rest.kudago-service")
public class KudaGoProperties {
    private String host;
    private Methods methods;

    @Getter
    @Setter
    public static class Methods {
        private Method categories;
        private Method locations;
        private Method events;

        @Getter
        @Setter
        public static class Method {
            private String type;
            private String uri;
        }
    }
}