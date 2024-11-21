package org.example.task8.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.task8.dto.ConvertCurrencyRequest;
import org.example.task8.dto.ConvertCurrencyResponse;
import org.example.task8.dto.CurrencyInfoDto;
import org.example.task8.exception.ServiceUnavailableException;
import org.example.task8.service.CurrencyService;
import org.example.task8.utils.validation.ValidationExceptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CurrencyController}
 */
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyController currencyController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private ValidationExceptionUtils validationExceptionUtils;

    private ConvertCurrencyRequest successConvertRequest;

    private ConvertCurrencyResponse convertCurrencyResponse;

    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17")
            .withUsername("postgres")
            .withPassword("123")
            .withDatabaseName("test");

    @SneakyThrows
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @BeforeEach
    public void setup() {
        successConvertRequest = new ConvertCurrencyRequest("USD", "EUR", BigDecimal.valueOf(1.0));
        convertCurrencyResponse = new ConvertCurrencyResponse("USD", "EUR", BigDecimal.valueOf(100.0));
    }

    @Test
    @DisplayName("Test convert successfully convert currency")
    @WithMockUser(roles = "USER")
    void convertCurrency_requestIsValid_shouldReturnOk() throws Exception {

        when(currencyService.convertCurrency(successConvertRequest)).thenReturn(convertCurrencyResponse);

        mockMvc.perform(post("/currencies/convert")
                        .content(objectMapper.writeValueAsString(successConvertRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.toCurrency").value(convertCurrencyResponse.toCurrency()))
                .andExpect(jsonPath("$.fromCurrency").value(convertCurrencyResponse.fromCurrency()))
                .andExpect(jsonPath("$.convertedAmount").value(convertCurrencyResponse.convertedAmount()));

        verify(currencyService).convertCurrency(successConvertRequest);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConvertRequests")
    @DisplayName("Test unsuccessful currency conversion")
    @WithMockUser(roles = "USER")
    void convertCurrency_requestHaveNullOrBlank_shouldReturnBadRequest(ConvertCurrencyRequest invalidRequest, HttpStatus expectedStatus) throws Exception {

        mockMvc.perform(post("/currencies/convert")
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus.value()));
    }

    private static Stream<Arguments> provideInvalidConvertRequests() {
        return Stream.of(
                Arguments.of(new ConvertCurrencyRequest("USD", "EUR", BigDecimal.valueOf(-1.0)), HttpStatus.BAD_REQUEST),
                Arguments.of(new ConvertCurrencyRequest("", "EUR", BigDecimal.valueOf(1.0)), HttpStatus.BAD_REQUEST),
                Arguments.of(new ConvertCurrencyRequest("USD", "", BigDecimal.valueOf(1.0)), HttpStatus.BAD_REQUEST),
                Arguments.of(new ConvertCurrencyRequest("USD", "EUR", null), HttpStatus.BAD_REQUEST)
        );
    }


    @Test
    @WithMockUser(roles = "USER")
    void getCurrencyInfo_requestIsValid_shouldReturnOk() throws Exception {

        String code = "USD";
        when(currencyService.getCurrencyInfo(code)).thenReturn(new CurrencyInfoDto(code, "1.0"));

        mockMvc.perform(get("/currencies/rates/{code}", code))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value(code))
                .andExpect(jsonPath("$.rate").value(1.0));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCurrencyInfo_circuitBreakerIsOpen_shouldReturnServiceUnavailable() throws Exception {
        String code = "USD";
        when(currencyService.getCurrencyInfo(code)).thenThrow(new ServiceUnavailableException("Service unavailable"));

        mockMvc.perform(get("/currencies/rates/{code}", code))
                .andExpect(status().is(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.message").value("Service unavailable"));


        verify(currencyService).getCurrencyInfo(code);
    }
}
