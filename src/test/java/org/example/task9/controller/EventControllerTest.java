package org.example.task9.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task9.model.Event;
import org.example.task9.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Event event1;
    private Event event2;

    @BeforeEach
    public void setup() {
        event1 = new Event(1L, "Concert", "100", false);
        event2 = new Event(2L, "Movie", "200", false);

    }

    @Test
    @DisplayName("Get events successfully")
    void getEvents_validRequest_shouldReturnOk() throws Exception {
        Mono<List<Event>> events = Mono.just(Arrays.asList(event1, event2));
        when(eventService.getEventsReactive(100.0, "USD", null, null)).thenReturn(events);

        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "100.0")
                        .param("currency", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(event1.id()))
                .andExpect(jsonPath("$[0].title").value(event1.title()))
                .andExpect(jsonPath("$[0].price").value(event1.price()))
                .andExpect(jsonPath("$[0].is_free").value(event1.isFree()));

        verify(eventService).getEventsReactive(100.0, "USD", null, null);
    }

    @Test
    @DisplayName("Get events with missing currency")
    void getEvents_missingCurrency_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "100.0")) // Missing currency
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get events with invalid currency")
    void getEvents_invalidCurrency_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "100.0")
                        .param("currency", "INVALID_CURRENCY")) // Invalid currency
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get events with date range")
    void getEvents_withDateRange_shouldReturnOk() throws Exception {
        Mono<List<Event>> events = Mono.just(Arrays.asList(event1, event2));
        when(eventService.getEventsReactive(100.0, "USD", LocalDate.of(2023, 10, 19), LocalDate.of(2023, 10, 21))).thenReturn(events);

        mockMvc.perform(get("/api/v1/events")
                        .param("budget", "100.0")
                        .param("currency", "USD")
                        .param("dateFrom", "2023-10-19")
                        .param("dateTo", "2023-10-21"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(eventService).getEventsReactive(100.0, "USD", LocalDate.of(2023, 10, 19), LocalDate.of(2023, 10, 21));
    }
}