package com.ynov.testing.controller;

import com.ynov.testing.model.Player;
import com.ynov.testing.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * PlayerController Class
 * 
 * This REST controller handles HTTP requests related to player operations.
 * It provides a RESTful API for managing players in the system.
 * 
 * This controller demonstrates:
 * - RESTful API design principles
 * - HTTP status codes and response handling
 * - Input validation with @Valid
 * - Error handling and exception management
 * - Different response formats (single object, lists, pagination)
 * 
 * API Endpoints:
 * - GET /api/players - Get all players (with optional pagination)
 * - GET /api/players/{id} - Get player by ID
 * - POST /api/players - Create new player
 * - PUT /api/players/{id} - Update existing player
 * - DELETE /api/players/{id} - Delete player
 * - GET /api/players/search/* - Various search endpoints
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/players")
@Validated
@CrossOrigin(origins = "*") // Allow CORS for frontend testing
public class PlayerController {

    private final PlayerService playerService;

    /**
     * Constructor injection for PlayerService.
     * 
     * @param playerService The player service
     */
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Get all players.
     * 
     * @param page Page number (optional, default: 0)
     * @param size Page size (optional, default: 10)
     * @param sortBy Field to sort by (optional, default: id)
     * @param sortDir Sort direction (optional, default: asc)
     * @return ResponseEntity with list of players or paginated result
     */
    @GetMapping
    public ResponseEntity<?> getAllPlayers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        try {
            if (page != null && size != null) {
                // Pagination requested
                Sort sort = sortDir.equalsIgnoreCase("desc") 
                    ? Sort.by(sortBy).descending() 
                    : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<Player> playersPage = playerService.getAllPlayers(pageable);

                return ResponseEntity.ok(playersPage);
            } else {
                // Return all players without pagination
                List<Player> players = playerService.getAllPlayers();
                return ResponseEntity.ok(players);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve players", "message", e.getMessage()));
        }
    }

    /**
     * Get a player by ID.
     * 
     * @param id The player ID
     * @return ResponseEntity with the player or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable @NotNull @Min(1) Long id) {
        try {
            Optional<Player> player = playerService.getPlayerById(id);

            if (player.isPresent()) {
                return ResponseEntity.ok(player.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Player not found", "id", id));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid request", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve player", "message", e.getMessage()));
        }
    }

    /**
     * Create a new player.
     * 
     * @param player The player data from request body
     * @return ResponseEntity with created player or error
     */
    @PostMapping
    public ResponseEntity<?> createPlayer(@Valid @RequestBody Player player) {
        try {
            Player createdPlayer = playerService.createPlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid player data", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create player", "message", e.getMessage()));
        }
    }

    /**
     * Update an existing player.
     * 
     * @param id The player ID
     * @param player The updated player data
     * @return ResponseEntity with updated player or error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable @NotNull @Min(1) Long id, 
                                         @Valid @RequestBody Player player) {
        try {
            Player updatedPlayer = playerService.updatePlayer(id, player);
            return ResponseEntity.ok(updatedPlayer);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Player not found", "id", id, "message", e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid player data", "message", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update player", "message", e.getMessage()));
        }
    }

    /**
     * Delete a player.
     * 
     * @param id The player ID
     * @return ResponseEntity with success message or error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable @NotNull @Min(1) Long id) {
        try {
            playerService.deletePlayer(id);
            return ResponseEntity.ok(Map.of("message", "Player deleted successfully", "id", id));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Player not found", "id", id));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid request", "message", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to delete player", "message", e.getMessage()));
        }
    }

    /**
     * Search players by team name.
     * 
     * @param teamName The team name
     * @return ResponseEntity with list of players in the team
     */
    @GetMapping("/search/team/{teamName}")
    public ResponseEntity<?> getPlayersByTeam(@PathVariable String teamName) {
        try {
            List<Player> players = playerService.getPlayersByTeam(teamName);
            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid team name", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search players", "message", e.getMessage()));
        }
    }

    /**
     * Search players by position.
     * 
     * @param position The position
     * @return ResponseEntity with list of players with the position
     */
    @GetMapping("/search/position/{position}")
    public ResponseEntity<?> getPlayersByPosition(@PathVariable String position) {
        try {
            List<Player> players = playerService.getPlayersByPosition(position);
            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid position", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search players", "message", e.getMessage()));
        }
    }

    /**
     * Search players by age range.
     * 
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @return ResponseEntity with list of players in the age range
     */
    @GetMapping("/search/age")
    public ResponseEntity<?> getPlayersByAgeRange(
            @RequestParam @NotNull @Min(0) Integer minAge,
            @RequestParam @NotNull @Min(0) Integer maxAge) {
        try {
            List<Player> players = playerService.getPlayersByAgeRange(minAge, maxAge);
            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid age range", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search players", "message", e.getMessage()));
        }
    }

    /**
     * Get active players.
     * 
     * @return ResponseEntity with list of active players
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActivePlayers() {
        try {
            List<Player> players = playerService.getActivePlayers();
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve active players", "message", e.getMessage()));
        }
    }

    /**
     * Get inactive players.
     * 
     * @return ResponseEntity with list of inactive players
     */
    @GetMapping("/inactive")
    public ResponseEntity<?> getInactivePlayers() {
        try {
            List<Player> players = playerService.getInactivePlayers();
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve inactive players", "message", e.getMessage()));
        }
    }

    /**
     * Deactivate a player.
     * 
     * @param id The player ID
     * @return ResponseEntity with updated player or error
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivatePlayer(@PathVariable @NotNull @Min(1) Long id) {
        try {
            Player updatedPlayer = playerService.deactivatePlayer(id);
            return ResponseEntity.ok(updatedPlayer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Player not found", "id", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to deactivate player", "message", e.getMessage()));
        }
    }

    /**
     * Activate a player.
     * 
     * @param id The player ID
     * @return ResponseEntity with updated player or error
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activatePlayer(@PathVariable @NotNull @Min(1) Long id) {
        try {
            Player updatedPlayer = playerService.activatePlayer(id);
            return ResponseEntity.ok(updatedPlayer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Player not found", "id", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to activate player", "message", e.getMessage()));
        }
    }

    /**
     * Get player count by team.
     * 
     * @param teamName The team name
     * @return ResponseEntity with player count
     */
    @GetMapping("/count/team/{teamName}")
    public ResponseEntity<?> getPlayerCountByTeam(@PathVariable String teamName) {
        try {
            long count = playerService.countPlayersByTeam(teamName);
            return ResponseEntity.ok(Map.of("teamName", teamName, "playerCount", count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid team name", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to count players", "message", e.getMessage()));
        }
    }

    /**
     * Get total active player count.
     * 
     * @return ResponseEntity with active player count
     */
    @GetMapping("/count/active")
    public ResponseEntity<?> getActivePlayerCount() {
        try {
            long count = playerService.countActivePlayers();
            return ResponseEntity.ok(Map.of("activePlayerCount", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to count active players", "message", e.getMessage()));
        }
    }

    /**
     * Search players by full name.
     * 
     * @param fullName The full name to search for
     * @return ResponseEntity with list of matching players
     */
    @GetMapping("/search/name")
    public ResponseEntity<?> searchPlayersByFullName(@RequestParam String fullName) {
        try {
            List<Player> players = playerService.findPlayersByFullName(fullName);
            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid full name", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search players", "message", e.getMessage()));
        }
    }

    /**
     * Get players with salary above a threshold.
     * 
     * @param minSalary The minimum salary threshold
     * @return ResponseEntity with list of players
     */
    @GetMapping("/search/salary")
    public ResponseEntity<?> getPlayersWithSalaryAbove(@RequestParam Double minSalary) {
        try {
            List<Player> players = playerService.getPlayersWithSalaryAbove(minSalary);
            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid salary value", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to search players", "message", e.getMessage()));
        }
    }

    /**
     * Get average age of players in a team.
     * 
     * @param teamName The team name
     * @return ResponseEntity with average age
     */
    @GetMapping("/stats/team/{teamName}/average-age")
    public ResponseEntity<?> getAverageAgeByTeam(@PathVariable String teamName) {
        try {
            Double averageAge = playerService.calculateAverageAgeByTeam(teamName);
            return ResponseEntity.ok(Map.of("teamName", teamName, "averageAge", averageAge));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid team name", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to calculate average age", "message", e.getMessage()));
        }
    }

    /**
     * Health check endpoint.
     * 
     * @return ResponseEntity with status information
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            long totalPlayers = playerService.getAllPlayers().size();
            long activePlayers = playerService.countActivePlayers();

            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "PlayerService",
                "totalPlayers", totalPlayers,
                "activePlayers", activePlayers,
                "timestamp", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("status", "DOWN", "error", e.getMessage()));
        }
    }
}
