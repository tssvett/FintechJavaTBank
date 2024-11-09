package org.example.task12.security.repository;

import org.example.task12.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByLogin(String login);
}
