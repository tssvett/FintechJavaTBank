package org.example.task9.dto;

import org.example.task9.model.Event;

import java.util.List;

public record EventResponse(
        int count,
        String next,
        String previous,
        List<Event> results
) {
}
