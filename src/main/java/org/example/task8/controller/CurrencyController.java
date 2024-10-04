package org.example.task8.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.task8.dto.ConvertCurrencyRequest;
import org.example.task8.dto.ConvertCurrencyResponse;
import org.example.task8.dto.CurrencyInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CurrencyController {

    @GetMapping("/currencies/rates/{code}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyInfoDto getCurrencyInfo(@PathVariable("code") String code) {

    }

    @PostMapping("/currencies/convert")
    @ResponseStatus(HttpStatus.OK)
    public ConvertCurrencyResponse convertCurrency(@RequestBody ConvertCurrencyRequest convertCurrencyRequest) {

    }

}
