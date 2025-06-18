package com.ynov.testing.repository;

import com.ynov.testing.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Event entity
 *
 * Students should implement this repository following Spring Data JPA conventions.
 * Reference: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Les étudiants doivent implémenter ces méthodes de requête personnalisées
    List<Event> findByActiveTrue();
    List<Event> findByEventDateAfter(LocalDateTime date);
    List<Event> findByEventDateBefore(LocalDateTime date);
    boolean existsByName(String name);
}
