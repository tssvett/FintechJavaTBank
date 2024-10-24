package org.example.task8.parser.xml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record Valute(
        @JacksonXmlProperty(localName = "ID", isAttribute = true) String id,
        @JacksonXmlProperty(localName = "NumCode") String numCode,
        @JacksonXmlProperty(localName = "CharCode") String charCode,
        @JacksonXmlProperty(localName = "Nominal") String nominal,
        @JacksonXmlProperty(localName = "Name") String name,
        @JacksonXmlProperty(localName = "Value") String value,
        @JacksonXmlProperty(localName = "VunitRate") String vunitRate) {
}
