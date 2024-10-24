package org.example.task8.mapper;

import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.parser.xml.model.Valute;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public CurrencyInfoDto toCurrencyInfoDto(Valute valute) {
        return new CurrencyInfoDto(valute.charCode(), valute.value());
    }
}
