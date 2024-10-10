package org.example.task8.parser.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "ValCurs")
public record ValCurs(
        @JacksonXmlProperty(localName = "Date", isAttribute = true) String date,
        @JacksonXmlProperty(localName = "name", isAttribute = true) String name,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "Valute") List<Valute> valute
) {
}