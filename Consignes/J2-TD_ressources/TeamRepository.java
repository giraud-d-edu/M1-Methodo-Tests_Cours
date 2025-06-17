package com.ynov.testing.repository;

import com.ynov.testing.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Team entity
 *
 * This repository provides data access operations for League of Legends teams.
 * It extends JpaRepository to inherit basic CRUD operations and defines
 * custom query methods for specific business requirements.
 *
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Check if a team exists with the given name
     *
     * @param name Team name to check
     * @return true if team exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find teams by region
     *
     * @param region Region code (e.g., "EU", "NA", "KR")
     * @return List of teams in the specified region
     */
    List<Team> findByRegion(String region);

    /**
     * Find all active teams
     *
     * @return List of active teams
     */
    List<Team> findByActiveTrue();

    // Note: Les méthodes suivantes sont héritées de JpaRepository<Team, Long>
    // et n'ont pas besoin d'être redéclarées :
    // - List<Team> findAll()
    // - Optional<Team> findById(Long id)
    // - Team save(Team team)
    // - void deleteById(Long id)
    // - long count()
    // - boolean existsById(Long id)
}
