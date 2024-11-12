package org.example.task8.parser.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.exception.XmlParserException;
import org.example.task8.parser.Parser;
import org.example.task8.parser.xml.model.ValCurs;
import org.example.task8.parser.xml.model.Valute;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class XmlParser implements Parser<List<Valute>> {
    private final XmlMapper xmlMapper;

    @Override
    public List<Valute> parse(String xml) {
        try {
            log.debug("XML: {}", xml);

            return xmlMapper.readValue(xml, ValCurs.class).valute();

        } catch (IOException e) {
            log.error("Error parsing XML: {}", e.getMessage());

            throw new XmlParserException("Failed to parse currencies from XML");
        }
    }
}
