package org.example.task10.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.task10.entity.Event;
import org.example.task10.entity.Place;
import org.example.task10.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EventSpecification {

    public Specification<Event> filterEventsBySearchCriteria(SearchCriteria searchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(searchCriteria.name())
                    .filter(name -> !name.isEmpty())
                    .ifPresent(name ->
                            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"))
                    );

            Optional.ofNullable(searchCriteria.place())
                    .filter(place -> !place.isEmpty())
                    .ifPresent(place -> {
                        Join<Event, Place> placeJoin = root.join("place");
                        predicates.add(criteriaBuilder.like(placeJoin.get("name"), "%" + place + "%"));
                    });

            Optional.ofNullable(searchCriteria.fromDate())
                    .ifPresent(fromDate ->
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate))
                    );

            Optional.ofNullable(searchCriteria.toDate())
                    .ifPresent(toDate ->
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate))
                    );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}