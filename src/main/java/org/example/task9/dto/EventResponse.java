package org.example.task9.dto;

import java.util.List;
import org.example.task9.model.ApiEvent;

public record EventResponse(
        int count,
        String next,
        String previous,
        List<ApiEvent> results
) {
}
