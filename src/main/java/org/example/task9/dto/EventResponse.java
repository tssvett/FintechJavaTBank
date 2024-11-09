package org.example.task9.dto;

import org.example.task9.model.ApiEvent;

import java.util.List;

public record EventResponse(
        int count,
        String next,
        String previous,
        List<ApiEvent> results
) {
}
