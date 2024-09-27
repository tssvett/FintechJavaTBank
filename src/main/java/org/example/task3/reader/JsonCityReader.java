package org.example.task3.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.task3.model.City;

import java.nio.file.Path;
import java.util.Optional;

@Slf4j
public class JsonCityReader {
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<City> readCityFromFile(Path path) {
        try {
            log.info("Starting reading file: {}", path.toAbsolutePath());
            City city = mapper.readValue(path.toFile(), City.class);
            log.debug("Result: {}", city.toString());
            log.info("Successfully finished reading file: {}", path.toAbsolutePath());

            return Optional.of(city);
        } catch (Exception e) {
            log.error("Failed to read file: {}", path.toAbsolutePath());
            log.error("Error: {}", e.getMessage());

            return Optional.empty();
        }
    }
}
