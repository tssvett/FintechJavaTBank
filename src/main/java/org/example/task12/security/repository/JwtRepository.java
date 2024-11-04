package org.example.task12.security.repository;

import org.example.task12.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<JwtToken, Long> {
}
