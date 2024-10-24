package org.example.task10.specification.criteria;

import java.time.LocalDate;

public record SearchCriteria(
        String name,
        String place,
        LocalDate fromDate,
        LocalDate toDate
) {
}
