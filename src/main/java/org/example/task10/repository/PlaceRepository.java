package org.example.task10.repository;

import jakarta.validation.constraints.NotNull;
import org.example.task10.enitiy.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT place FROM Place place JOIN FETCH place.events WHERE place.id = :id")
    Optional<Place> findById(long id);
}
