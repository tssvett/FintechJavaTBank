package org.example.task12.security.repository;

import org.example.task12.entity.ApiUser;
import org.example.task12.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);

    List<JwtToken> findAllByUserAndRevoked(ApiUser user, boolean revoked);
}
