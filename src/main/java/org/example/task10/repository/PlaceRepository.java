package org.example.task10.repository;

import java.util.Optional;
import java.util.UUID;
import org.example.task10.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {

    @Query("SELECT place FROM Place place JOIN FETCH place.events WHERE place.id = :id")
    Optional<Place> findById(UUID id);
}
