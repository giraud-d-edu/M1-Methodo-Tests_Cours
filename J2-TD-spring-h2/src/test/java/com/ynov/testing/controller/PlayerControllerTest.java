package com.ynov.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.testing.model.Player;
import com.ynov.testing.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Imports Hamcrest pour les matchers JSON
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

// Imports Mockito pour les mocks et argument matchers
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Imports Spring Test pour MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit Tests for PlayerController
 *
 * This class demonstrates web layer testing with Spring Boot:
 * - Using @WebMvcTest for testing only the web layer
 * - Mocking service dependencies with @MockitoBean
 * - Using MockMvc for HTTP request simulation
 * - Testing JSON serialization/deserialization
 * - Testing different HTTP status codes
 * - Using Hamcrest matchers for JSON path assertions
 *
 * These tests focus on testing the controller layer in isolation
 * by mocking the service dependency.
 *
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@WebMvcTest(PlayerController.class)
@DisplayName("Player Controller Unit Tests")
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player testPlayer;
    private Player savedPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player("John", "Doe", "john.doe@example.com", 25, "Forward");
        testPlayer.setTeamName("Test Team");
        testPlayer.setJerseyNumber(10);
        testPlayer.setSalary(50000.0);

        savedPlayer = new Player("John", "Doe", "john.doe@example.com", 25, "Forward");
        savedPlayer.setId(1L);
        savedPlayer.setTeamName("Test Team");
        savedPlayer.setJerseyNumber(10);
        savedPlayer.setSalary(50000.0);
    }

    @Test
    @DisplayName("GET /api/players should return all players")
    void getAllPlayers_ShouldReturnPlayerList() throws Exception {
        // Given
        List<Player> players = Arrays.asList(savedPlayer,
                createPlayer(2L, "Jane", "Smith", "jane.smith@example.com", 23, "Midfielder"));
        when(playerService.getAllPlayers()).thenReturn(players);

        // When & Then
        mockMvc.perform(get("/api/players"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));

        verify(playerService).getAllPlayers();
    }

    @Test
    @DisplayName("GET /api/players/{id} should return player when exists")
    void getPlayerById_WithExistingId_ShouldReturnPlayer() throws Exception {
        // Given
        Long playerId = 1L;
        when(playerService.getPlayerById(playerId)).thenReturn(Optional.of(savedPlayer));

        // When & Then
        mockMvc.perform(get("/api/players/{id}", playerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$.position", is("Forward")))
                .andExpect(jsonPath("$.teamName", is("Test Team")))
                .andExpect(jsonPath("$.jerseyNumber", is(10)))
                .andExpect(jsonPath("$.salary", is(50000.0)));

        verify(playerService).getPlayerById(playerId);
    }

    @Test
    @DisplayName("GET /api/players/{id} should return 404 when player not found")
    void getPlayerById_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given
        Long playerId = 999L;
        when(playerService.getPlayerById(playerId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/players/{id}", playerId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Player not found")))
                .andExpect(jsonPath("$.id", is(999)));

        verify(playerService).getPlayerById(playerId);
    }

    @Test
    @DisplayName("GET /api/players/{id} should return 400 for invalid ID")
    void getPlayerById_WithInvalidId_ShouldReturn400() throws Exception {
        // Given
        Long invalidId = -1L;
        when(playerService.getPlayerById(invalidId))
                .thenThrow(new IllegalArgumentException("Player ID must be positive"));

        // When & Then
        mockMvc.perform(get("/api/players/{id}", invalidId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Invalid request")))
                .andExpect(jsonPath("$.message", is("Player ID must be positive")));

        verify(playerService).getPlayerById(invalidId);
    }

    @Test
    @DisplayName("POST /api/players should create player successfully")
    void createPlayer_WithValidData_ShouldReturnCreatedPlayer() throws Exception {
        // Given
        when(playerService.createPlayer(any(Player.class))).thenReturn(savedPlayer);

        // When & Then
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(playerService).createPlayer(any(Player.class));
    }

    @Test
    @DisplayName("POST /api/players should return 400 for invalid player data")
    void createPlayer_WithInvalidData_ShouldReturn400() throws Exception {
        // Given
        Player invalidPlayer = new Player(); // Missing required fields
        when(playerService.createPlayer(any(Player.class)))
                .thenThrow(new IllegalArgumentException("First name is required"));

        // When & Then
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPlayer)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Invalid player data")))
                .andExpect(jsonPath("$.message", is("First name is required")));

        verify(playerService).createPlayer(any(Player.class));
    }

    @Test
    @DisplayName("PUT /api/players/{id} should update player successfully")
    void updatePlayer_WithValidData_ShouldReturnUpdatedPlayer() throws Exception {
        // Given
        Long playerId = 1L;
        Player updatedData = new Player("John", "Smith", "john.smith@example.com", 26, "Defender");
        updatedData.setSalary(60000.0);

        Player updatedPlayer = new Player("John", "Smith", "john.smith@example.com", 26, "Defender");
        updatedPlayer.setId(1L);
        updatedPlayer.setSalary(60000.0);

        when(playerService.updatePlayer(eq(playerId), any(Player.class))).thenReturn(updatedPlayer);

        // When & Then
        mockMvc.perform(put("/api/players/{id}", playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("john.smith@example.com")))
                .andExpect(jsonPath("$.age", is(26)))
                .andExpect(jsonPath("$.position", is("Defender")))
                .andExpect(jsonPath("$.salary", is(60000.0)));

        verify(playerService).updatePlayer(eq(playerId), any(Player.class));
    }

    @Test
    @DisplayName("PUT /api/players/{id} should return 404 when player not found")
    void updatePlayer_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given
        Long playerId = 999L;
        when(playerService.updatePlayer(eq(playerId), any(Player.class)))
                .thenThrow(new IllegalArgumentException("Player not found with ID: 999"));

        // When & Then
        mockMvc.perform(put("/api/players/{id}", playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Player not found")))
                .andExpect(jsonPath("$.id", is(999)));

        verify(playerService).updatePlayer(eq(playerId), any(Player.class));
    }

    @Test
    @DisplayName("DELETE /api/players/{id} should delete player successfully")
    void deletePlayer_WithExistingId_ShouldReturnSuccessMessage() throws Exception {
        // Given
        Long playerId = 1L;
        doNothing().when(playerService).deletePlayer(playerId);

        // When & Then
        mockMvc.perform(delete("/api/players/{id}", playerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Player deleted successfully")))
                .andExpect(jsonPath("$.id", is(1)));

        verify(playerService).deletePlayer(playerId);
    }

    @Test
    @DisplayName("DELETE /api/players/{id} should return 404 when player not found")
    void deletePlayer_WithNonExistentId_ShouldReturn404() throws Exception {
        // Given
        Long playerId = 999L;
        doThrow(new IllegalArgumentException("Player not found with ID: 999"))
                .when(playerService).deletePlayer(playerId);

        // When & Then
        mockMvc.perform(delete("/api/players/{id}", playerId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Player not found")))
                .andExpect(jsonPath("$.id", is(999)));

        verify(playerService).deletePlayer(playerId);
    }

    @Test
    @DisplayName("GET /api/players/search/team/{teamName} should return players by team")
    void getPlayersByTeam_ShouldReturnTeamPlayers() throws Exception {
        // Given
        String teamName = "Test Team";
        List<Player> teamPlayers = Arrays.asList(savedPlayer);
        when(playerService.getPlayersByTeam(teamName)).thenReturn(teamPlayers);

        // When & Then
        mockMvc.perform(get("/api/players/search/team/{teamName}", teamName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].teamName", is("Test Team")));

        verify(playerService).getPlayersByTeam(teamName);
    }

    @Test
    @DisplayName("GET /api/players/search/position/{position} should return players by position")
    void getPlayersByPosition_ShouldReturnPlayersWithPosition() throws Exception {
        // Given
        String position = "Forward";
        List<Player> forwards = Arrays.asList(savedPlayer);
        when(playerService.getPlayersByPosition(position)).thenReturn(forwards);

        // When & Then
        mockMvc.perform(get("/api/players/search/position/{position}", position))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].position", is("Forward")));

        verify(playerService).getPlayersByPosition(position);
    }

    @Test
    @DisplayName("GET /api/players/search/age should return players by age range")
    void getPlayersByAgeRange_ShouldReturnPlayersInRange() throws Exception {
        // Given
        Integer minAge = 20;
        Integer maxAge = 30;
        List<Player> playersInRange = Arrays.asList(savedPlayer);
        when(playerService.getPlayersByAgeRange(minAge, maxAge)).thenReturn(playersInRange);

        // When & Then
        mockMvc.perform(get("/api/players/search/age")
                        .param("minAge", minAge.toString())
                        .param("maxAge", maxAge.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].age", is(25)));

        verify(playerService).getPlayersByAgeRange(minAge, maxAge);
    }

    @Test
    @DisplayName("GET /api/players/active should return active players")
    void getActivePlayers_ShouldReturnActivePlayersOnly() throws Exception {
        // Given
        List<Player> activePlayers = Arrays.asList(savedPlayer);
        when(playerService.getActivePlayers()).thenReturn(activePlayers);

        // When & Then
        mockMvc.perform(get("/api/players/active"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(playerService).getActivePlayers();
    }

    @Test
    @DisplayName("PATCH /api/players/{id}/deactivate should deactivate player")
    void deactivatePlayer_ShouldReturnDeactivatedPlayer() throws Exception {
        // Given
        Long playerId = 1L;
        Player deactivatedPlayer = new Player("John", "Doe", "john.doe@example.com", 25, "Forward");
        deactivatedPlayer.setId(1L);
        deactivatedPlayer.setActive(false);

        when(playerService.deactivatePlayer(playerId)).thenReturn(deactivatedPlayer);

        // When & Then
        mockMvc.perform(patch("/api/players/{id}/deactivate", playerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.active", is(false)));

        verify(playerService).deactivatePlayer(playerId);
    }

    @Test
    @DisplayName("GET /api/players/count/team/{teamName} should return team player count")
    void getPlayerCountByTeam_ShouldReturnCount() throws Exception {
        // Given
        String teamName = "Test Team";
        long playerCount = 5L;
        when(playerService.countPlayersByTeam(teamName)).thenReturn(playerCount);

        // When & Then
        mockMvc.perform(get("/api/players/count/team/{teamName}", teamName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.teamName", is("Test Team")))
                .andExpect(jsonPath("$.playerCount", is(5)));

        verify(playerService).countPlayersByTeam(teamName);
    }

    @Test
    @DisplayName("GET /api/players/health should return service health status")
    void healthCheck_ShouldReturnHealthStatus() throws Exception {
        // Given
        List<Player> allPlayers = Arrays.asList(savedPlayer,
                createPlayer(2L, "Jane", "Smith", "jane@example.com", 23, "Midfielder"));
        when(playerService.getAllPlayers()).thenReturn(allPlayers);
        when(playerService.countActivePlayers()).thenReturn(2L);

        // When & Then
        mockMvc.perform(get("/api/players/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("PlayerService")))
                .andExpect(jsonPath("$.totalPlayers", is(2)))
                .andExpect(jsonPath("$.activePlayers", is(2)));

        verify(playerService).getAllPlayers();
        verify(playerService).countActivePlayers();
    }

    // Helper method to create test players
    private Player createPlayer(Long id, String firstName, String lastName,
                                String email, Integer age, String position) {
        Player player = new Player(firstName, lastName, email, age, position);
        player.setId(id);
        return player;
    }
}
