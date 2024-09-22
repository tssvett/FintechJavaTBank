package org.example.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.example.model.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class XmlCityWriter {

    public void writeCityToFile(City city, Path path) {
        log.info("Starting writing to file: {}", path.toAbsolutePath());
        try {
            String xmlCity = city.toXML();
            log.debug("Converted city to XML: {}", xmlCity);
            Files.writeString(path, city.toXML());
            log.info("Successfully finished writing file: {}", path.toAbsolutePath());
        } catch (JsonProcessingException e) {
            log.error("Error converting to XML: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Error saving to XML: {}", e.getMessage());
        }
    }
}
