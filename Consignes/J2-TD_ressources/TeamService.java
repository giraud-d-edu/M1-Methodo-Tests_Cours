package com.ynov.testing.service;

import com.ynov.testing.model.Team;
import com.ynov.testing.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Team entities
 *
 * This service provides CRUD operations for League of Legends teams
 * and includes business logic for team management.
 *
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Retrieve all teams from the database
     *
     * @return List of all teams
     */
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Find a team by its ID
     *
     * @param id Team identifier
     * @return Optional containing the team if found
     * @throws IllegalArgumentException if ID is null or negative
     */
    public Optional<Team> getTeamById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Team ID must be positive");
        }
        return teamRepository.findById(id);
    }

    /**
     * Create a new team
     *
     * @param team Team to create
     * @return Created team with generated ID
     * @throws IllegalArgumentException if team data is invalid
     */
    public Team createTeam(Team team) {
        validateTeam(team);

        // Check if team name already exists
        if (teamRepository.existsByName(team.getName())) {
            throw new IllegalArgumentException("Team name already exists: " + team.getName());
        }

        return teamRepository.save(team);
    }

    /**
     * Delete a team by ID
     *
     * @param id Team identifier
     * @throws IllegalArgumentException if team not found
     */
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team not found with ID: " + id);
        }
        teamRepository.deleteById(id);
    }

    /**
     * Update an existing team (TO BE IMPLEMENTED - TDD Exercise)
     *
     * @param id Team ID to update
     * @param teamData Updated team data
     * @return Updated team
     */
    public Team updateTeam(Long id, Team teamData) {
        // TODO: Implementation will be done using TDD approach
        // Students will implement this method following TDD methodology
        throw new UnsupportedOperationException("Method not implemented yet - TDD exercise");
    }

    // Méthodes utilitaires privées
    private void validateTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        if (team.getName() == null || team.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Team name is required");
        }
        if (team.getRegion() == null || team.getRegion().trim().isEmpty()) {
            throw new IllegalArgumentException("Team region is required");
        }
        if (team.getFoundedDate() == null) {
            throw new IllegalArgumentException("Founded date is required");
        }
    }

    // Méthodes de recherche additionnelles
    public List<Team> getTeamsByRegion(String region) {
        return teamRepository.findByRegion(region);
    }

    public List<Team> getActiveTeams() {
        return teamRepository.findByActiveTrue();
    }

    public long countTeams() {
        return teamRepository.count();
    }
}
