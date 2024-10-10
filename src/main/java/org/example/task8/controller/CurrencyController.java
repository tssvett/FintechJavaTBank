package org.example.task8.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task8.advice.details.ExceptionDetails;
import org.example.task8.currency.validator.ValidCurrency;
import org.example.task8.dto.ConvertCurrencyRequest;
import org.example.task8.dto.ConvertCurrencyResponse;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated

public class CurrencyController {
    private final CurrencyService currencyService;

    @Operation(summary = "Get currency information by code",
            description = "Retrieves detailed information about a currency based on its code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved currency information",
                    content = @Content(schema = @Schema(implementation = CurrencyInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "Currency not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "404", description = "Currency not exist",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable, try again later",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class)))
    })


    @GetMapping(value = "/currencies/rates/{code}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyInfoDto getCurrencyInfo(
            @Parameter(description = "The currency code (e.g., USD, EUR)", required = true)
            @Valid @ValidCurrency @PathVariable("code") String code) {
        return currencyService.getCurrencyInfo(code);
    }

    @Operation(summary = "Convert currency",
            description = "Converts an amount from one currency to another.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully converted currency",
                    content = @Content(schema = @Schema(implementation = ConvertCurrencyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "404", description = "One of the currencies not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable, try again later",
                    content = @Content(schema = @Schema(implementation = ExceptionDetails.class)))
    })


    @PostMapping("/currencies/convert")
    @ResponseStatus(HttpStatus.OK)
    public ConvertCurrencyResponse convertCurrency(
            @Valid @RequestBody ConvertCurrencyRequest convertCurrencyRequest) {
        return currencyService.convertCurrency(convertCurrencyRequest);
    }
}