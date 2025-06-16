package com.ynov.testing.service;

import com.ynov.testing.model.Player;
import com.ynov.testing.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PlayerService Class
 * 
 * This service class contains the business logic for player operations.
 * It acts as an intermediary between the controller and repository layers.
 * 
 * This service demonstrates:
 * - Business logic implementation
 * - Transaction management with @Transactional
 * - Input validation and business rules
 * - Exception handling
 * - Complex operations combining multiple repository calls
 * 
 * This class is perfect for testing service layer functionality.
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Constructor injection for PlayerRepository.
     * 
     * @param playerRepository The player repository
     */
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Create a new player.
     * 
     * @param player The player to create
     * @return The created player with generated ID
     * @throws IllegalArgumentException if player data is invalid
     */
    public Player createPlayer(Player player) {
        validatePlayerForCreation(player);

        // Check if email already exists
        if (playerRepository.existsByEmail(player.getEmail())) {
            throw new IllegalArgumentException("Player with email " + player.getEmail() + " already exists");
        }

        // Check jersey number uniqueness within team
        if (player.getTeamName() != null && player.getJerseyNumber() != null) {
            if (playerRepository.existsByJerseyNumberAndTeamName(player.getJerseyNumber(), player.getTeamName())) {
                throw new IllegalArgumentException("Jersey number " + player.getJerseyNumber() + 
                    " is already taken in team " + player.getTeamName());
            }
        }

        // Set default values
        if (player.getActive() == null) {
            player.setActive(true);
        }

        return playerRepository.save(player);
    }

    /**
     * Get all players.
     * 
     * @return List of all players
     */
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Get all players with pagination.
     * 
     * @param pageable Pagination information
     * @return Page of players
     */
    @Transactional(readOnly = true)
    public Page<Player> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    /**
     * Get a player by ID.
     * 
     * @param id The player ID
     * @return Optional containing the player if found
     */
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Player ID must be positive");
        }
        return playerRepository.findById(id);
    }

    /**
     * Get a player by email.
     * 
     * @param email The player email
     * @return Optional containing the player if found
     */
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return playerRepository.findByEmail(email.trim().toLowerCase());
    }

    /**
     * Update an existing player.
     * 
     * @param id The player ID
     * @param updatedPlayer The updated player data
     * @return The updated player
     * @throws IllegalArgumentException if player not found or data is invalid
     */
    public Player updatePlayer(Long id, Player updatedPlayer) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Player ID must be positive");
        }

        Player existingPlayer = playerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + id));

        validatePlayerForUpdate(updatedPlayer, existingPlayer);

        // Check email uniqueness (excluding current player)
        if (!existingPlayer.getEmail().equals(updatedPlayer.getEmail()) && 
            playerRepository.existsByEmail(updatedPlayer.getEmail())) {
            throw new IllegalArgumentException("Player with email " + updatedPlayer.getEmail() + " already exists");
        }

        // Check jersey number uniqueness within team (excluding current player)
        if (updatedPlayer.getTeamName() != null && updatedPlayer.getJerseyNumber() != null) {
            boolean jerseyExists = playerRepository.existsByJerseyNumberAndTeamName(
                updatedPlayer.getJerseyNumber(), updatedPlayer.getTeamName());
            if (jerseyExists && (!existingPlayer.getTeamName().equals(updatedPlayer.getTeamName()) || 
                !existingPlayer.getJerseyNumber().equals(updatedPlayer.getJerseyNumber()))) {
                throw new IllegalArgumentException("Jersey number " + updatedPlayer.getJerseyNumber() + 
                    " is already taken in team " + updatedPlayer.getTeamName());
            }
        }

        // Update fields
        existingPlayer.setFirstName(updatedPlayer.getFirstName());
        existingPlayer.setLastName(updatedPlayer.getLastName());
        existingPlayer.setEmail(updatedPlayer.getEmail());
        existingPlayer.setAge(updatedPlayer.getAge());
        existingPlayer.setPosition(updatedPlayer.getPosition());
        existingPlayer.setTeamName(updatedPlayer.getTeamName());
        existingPlayer.setJerseyNumber(updatedPlayer.getJerseyNumber());
        existingPlayer.setSalary(updatedPlayer.getSalary());
        existingPlayer.setActive(updatedPlayer.getActive());
        existingPlayer.setUpdatedAt(LocalDateTime.now());

        return playerRepository.save(existingPlayer);
    }

    /**
     * Delete a player by ID.
     * 
     * @param id The player ID
     * @throws IllegalArgumentException if player not found
     */
    public void deletePlayer(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Player ID must be positive");
        }

        if (!playerRepository.existsById(id)) {
            throw new IllegalArgumentException("Player not found with ID: " + id);
        }

        playerRepository.deleteById(id);
    }

    /**
     * Get players by team name.
     * 
     * @param teamName The team name
     * @return List of players in the team
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByTeam(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        return playerRepository.findByTeamName(teamName.trim());
    }

    /**
     * Get players by position.
     * 
     * @param position The position
     * @return List of players with the specified position
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }
        return playerRepository.findByPosition(position.trim());
    }

    /**
     * Get players by age range.
     * 
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @return List of players within the age range
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersByAgeRange(Integer minAge, Integer maxAge) {
        if (minAge == null || maxAge == null) {
            throw new IllegalArgumentException("Age range bounds cannot be null");
        }
        if (minAge < 0 || maxAge < 0) {
            throw new IllegalArgumentException("Age values must be non-negative");
        }
        if (minAge > maxAge) {
            throw new IllegalArgumentException("Minimum age cannot be greater than maximum age");
        }
        return playerRepository.findByAgeBetween(minAge, maxAge);
    }

    /**
     * Get active players.
     * 
     * @return List of active players
     */
    @Transactional(readOnly = true)
    public List<Player> getActivePlayers() {
        return playerRepository.findByActive(true);
    }

    /**
     * Get inactive players.
     * 
     * @return List of inactive players
     */
    @Transactional(readOnly = true)
    public List<Player> getInactivePlayers() {
        return playerRepository.findByActive(false);
    }

    /**
     * Deactivate a player.
     * 
     * @param id The player ID
     * @return The updated player
     */
    public Player deactivatePlayer(Long id) {
        Player player = getPlayerById(id)
            .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + id));

        player.setActive(false);
        player.setUpdatedAt(LocalDateTime.now());

        return playerRepository.save(player);
    }

    /**
     * Activate a player.
     * 
     * @param id The player ID
     * @return The updated player
     */
    public Player activatePlayer(Long id) {
        Player player = getPlayerById(id)
            .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + id));

        player.setActive(true);
        player.setUpdatedAt(LocalDateTime.now());

        return playerRepository.save(player);
    }

    /**
     * Count players in a team.
     * 
     * @param teamName The team name
     * @return Number of players in the team
     */
    @Transactional(readOnly = true)
    public long countPlayersByTeam(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        return playerRepository.countByTeamName(teamName.trim());
    }

    /**
     * Count total active players.
     * 
     * @return Number of active players
     */
    @Transactional(readOnly = true)
    public long countActivePlayers() {
        return playerRepository.countByActive(true);
    }

    /**
     * Find players by full name (first name + last name).
     * 
     * @param fullName The full name to search for
     * @return List of players matching the full name
     */
    @Transactional(readOnly = true)
    public List<Player> findPlayersByFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        return playerRepository.findByFullNameContaining(fullName.trim());
    }

    /**
     * Get players with salary above a certain amount.
     * 
     * @param minSalary The minimum salary threshold
     * @return List of players with salary above the threshold
     */
    @Transactional(readOnly = true)
    public List<Player> getPlayersWithSalaryAbove(Double minSalary) {
        if (minSalary == null || minSalary < 0) {
            throw new IllegalArgumentException("Minimum salary must be non-negative");
        }
        return playerRepository.findPlayersWithSalaryAbove(minSalary);
    }

    /**
     * Calculate average age of players in a team.
     * 
     * @param teamName The team name
     * @return Average age of players in the team
     */
    @Transactional(readOnly = true)
    public Double calculateAverageAgeByTeam(String teamName) {
        List<Player> players = getPlayersByTeam(teamName);
        if (players.isEmpty()) {
            return 0.0;
        }
        return players.stream().mapToInt(Player::getAge).average().orElse(0.0);
    }

    /**
     * Validate player data for creation.
     * 
     * @param player The player to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validatePlayerForCreation(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        validateCommonPlayerData(player);
    }

    /**
     * Validate player data for update.
     * 
     * @param player The player to validate
     * @param existingPlayer The existing player (for comparison)
     * @throws IllegalArgumentException if validation fails
     */
    private void validatePlayerForUpdate(Player player, Player existingPlayer) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        validateCommonPlayerData(player);
    }

    /**
     * Common validation logic for player data.
     * 
     * @param player The player to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCommonPlayerData(Player player) {
        if (player.getFirstName() == null || player.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (player.getLastName() == null || player.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (player.getEmail() == null || player.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (player.getAge() == null || player.getAge() < 0 || player.getAge() > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        if (player.getPosition() == null || player.getPosition().trim().isEmpty()) {
            throw new IllegalArgumentException("Position is required");
        }
        if (player.getSalary() != null && player.getSalary() < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        if (player.getJerseyNumber() != null && (player.getJerseyNumber() < 1 || player.getJerseyNumber() > 99)) {
            throw new IllegalArgumentException("Jersey number must be between 1 and 99");
        }
    }
}
