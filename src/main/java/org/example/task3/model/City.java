package org.example.task3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class City {
    private String slug;
    @JsonProperty("coords")
    private Coordinates coordinates;

    public String toXml() {
        return String.format(
                """
                        <City>
                            <slug>%s</slug>
                            <coords>
                                <lat>%s</lat>
                                <lon>%s</lon>
                            </coords>
                        </City>
                        """,
                slug, coordinates.getLat(), coordinates.getLon());
    }
}
