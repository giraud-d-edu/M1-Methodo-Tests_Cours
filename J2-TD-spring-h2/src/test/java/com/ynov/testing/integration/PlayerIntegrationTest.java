package com.ynov.testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.testing.model.Player;
import com.ynov.testing.repository.PlayerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Complete Integration Tests for Player API
 * 
 * This class demonstrates full application testing:
 * - Using @SpringBootTest with RANDOM_PORT for complete application context
 * - Testing with TestRestTemplate for real HTTP requests
 * - Testing complete CRUD operations end-to-end
 * - Validating HTTP status codes and response bodies
 * - Testing error scenarios and edge cases
 * 
 * These tests run against the complete application stack:
 * - Spring Boot application context
 * - Embedded web server
 * - H2 in-memory database
 * - All layers: Controller → Service → Repository → Database
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Player API Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlayerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/players";

        // Clean database before each test
        playerRepository.deleteAll();

        // Create test data
        testPlayer = new Player("John", "Doe", "john.doe@example.com", 25, "Forward");
        testPlayer.setTeamName("Test Team");
        testPlayer.setJerseyNumber(10);
        testPlayer.setSalary(50000.0);
    }

    @Test
    @Order(1)
    @DisplayName("Should create player via POST /api/players")
    void createPlayer_ShouldReturnCreatedPlayer() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Player> request = new HttpEntity<>(testPlayer, headers);

        // When
        ResponseEntity<Player> response = restTemplate.postForEntity(baseUrl, request, Player.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        Player createdPlayer = response.getBody();
        assertNotNull(createdPlayer.getId());
        assertEquals(testPlayer.getFirstName(), createdPlayer.getFirstName());
        assertEquals(testPlayer.getLastName(), createdPlayer.getLastName());
        assertEquals(testPlayer.getEmail(), createdPlayer.getEmail());
        assertEquals(testPlayer.getAge(), createdPlayer.getAge());
        assertEquals(testPlayer.getPosition(), createdPlayer.getPosition());
        assertTrue(createdPlayer.getActive());

        // Verify in database
        assertTrue(playerRepository.existsByEmail(testPlayer.getEmail()));
    }

    @Test
    @Order(2)
    @DisplayName("Should return 400 when creating player with invalid data")
    void createPlayer_WithInvalidData_ShouldReturn400() {
        // Given - Player with missing required fields
        Player invalidPlayer = new Player();
        invalidPlayer.setFirstName(""); // Invalid: empty name

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Player> request = new HttpEntity<>(invalidPlayer, headers);

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl, request, Map.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    @Order(3)
    @DisplayName("Should get all players via GET /api/players")
    void getAllPlayers_ShouldReturnPlayerList() {
        // Given - Create test players in database
        Player player1 = playerRepository.save(testPlayer);
        Player player2 = new Player("Jane", "Smith", "jane.smith@example.com", 23, "Midfielder");
        playerRepository.save(player2);

        // When
        ResponseEntity<Player[]> response = restTemplate.getForEntity(baseUrl, Player[].class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player[] players = response.getBody();
        assertEquals(2, players.length);

        // Verify player data
        boolean foundPlayer1 = false;
        boolean foundPlayer2 = false;
        for (Player player : players) {
            if (player.getEmail().equals("john.doe@example.com")) {
                foundPlayer1 = true;
                assertEquals("John", player.getFirstName());
            }
            if (player.getEmail().equals("jane.smith@example.com")) {
                foundPlayer2 = true;
                assertEquals("Jane", player.getFirstName());
            }
        }
        assertTrue(foundPlayer1, "Should find John Doe");
        assertTrue(foundPlayer2, "Should find Jane Smith");
    }

    @Test
    @Order(4)
    @DisplayName("Should get player by ID via GET /api/players/{id}")
    void getPlayerById_WithValidId_ShouldReturnPlayer() {
        // Given
        Player savedPlayer = playerRepository.save(testPlayer);

        // When
        ResponseEntity<Player> response = restTemplate.getForEntity(
            baseUrl + "/" + savedPlayer.getId(), 
            Player.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player returnedPlayer = response.getBody();
        assertEquals(savedPlayer.getId(), returnedPlayer.getId());
        assertEquals(savedPlayer.getFirstName(), returnedPlayer.getFirstName());
        assertEquals(savedPlayer.getEmail(), returnedPlayer.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Should return 404 when getting player with non-existent ID")
    void getPlayerById_WithNonExistentId_ShouldReturn404() {
        // Given
        Long nonExistentId = 999L;

        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/" + nonExistentId, 
            Map.class
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Player not found", response.getBody().get("error"));
    }

    @Test
    @Order(6)
    @DisplayName("Should update player via PUT /api/players/{id}")
    void updatePlayer_WithValidData_ShouldReturnUpdatedPlayer() {
        // Given
        Player savedPlayer = playerRepository.save(testPlayer);

        Player updateData = new Player("John", "Smith", "john.smith@example.com", 26, "Defender");
        updateData.setSalary(60000.0);
        updateData.setTeamName("Updated Team");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Player> request = new HttpEntity<>(updateData, headers);

        // When
        ResponseEntity<Player> response = restTemplate.exchange(
            baseUrl + "/" + savedPlayer.getId(),
            HttpMethod.PUT,
            request,
            Player.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player updatedPlayer = response.getBody();
        assertEquals(savedPlayer.getId(), updatedPlayer.getId());
        assertEquals("Smith", updatedPlayer.getLastName());
        assertEquals("john.smith@example.com", updatedPlayer.getEmail());
        assertEquals(26, updatedPlayer.getAge());
        assertEquals("Defender", updatedPlayer.getPosition());
        assertEquals(60000.0, updatedPlayer.getSalary());
        assertEquals("Updated Team", updatedPlayer.getTeamName());

        // Verify in database
        Player dbPlayer = playerRepository.findById(savedPlayer.getId()).orElse(null);
        assertNotNull(dbPlayer);
        assertEquals("Smith", dbPlayer.getLastName());
    }

    @Test
    @Order(7)
    @DisplayName("Should delete player via DELETE /api/players/{id}")
    void deletePlayer_WithValidId_ShouldDeletePlayer() {
        // Given
        Player savedPlayer = playerRepository.save(testPlayer);
        Long playerId = savedPlayer.getId();

        // When
        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/" + playerId,
            HttpMethod.DELETE,
            null,
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertEquals("Player deleted successfully", response.getBody().get("message"));

        // Verify player is deleted from database
        assertFalse(playerRepository.existsById(playerId));
    }

    @Test
    @Order(8)
    @DisplayName("Should search players by team via GET /api/players/search/team/{teamName}")
    void searchPlayersByTeam_ShouldReturnTeamPlayers() {
        // Given
        Player player1 = new Player("John", "Doe", "john@example.com", 25, "Forward");
        player1.setTeamName("Team A");
        playerRepository.save(player1);

        Player player2 = new Player("Jane", "Smith", "jane@example.com", 23, "Midfielder");
        player2.setTeamName("Team A");
        playerRepository.save(player2);

        Player player3 = new Player("Bob", "Johnson", "bob@example.com", 28, "Defender");
        player3.setTeamName("Team B");
        playerRepository.save(player3);

        // When
        ResponseEntity<Player[]> response = restTemplate.getForEntity(
            baseUrl + "/search/team/Team A", 
            Player[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player[] teamPlayers = response.getBody();
        assertEquals(2, teamPlayers.length);

        for (Player player : teamPlayers) {
            assertEquals("Team A", player.getTeamName());
        }
    }

    @Test
    @Order(9)
    @DisplayName("Should search players by position via GET /api/players/search/position/{position}")
    void searchPlayersByPosition_ShouldReturnPlayersWithPosition() {
        // Given
        Player forward1 = new Player("John", "Doe", "john@example.com", 25, "Forward");
        playerRepository.save(forward1);

        Player forward2 = new Player("Jane", "Smith", "jane@example.com", 23, "Forward");
        playerRepository.save(forward2);

        Player midfielder = new Player("Bob", "Johnson", "bob@example.com", 28, "Midfielder");
        playerRepository.save(midfielder);

        // When
        ResponseEntity<Player[]> response = restTemplate.getForEntity(
            baseUrl + "/search/position/Forward", 
            Player[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player[] forwards = response.getBody();
        assertEquals(2, forwards.length);

        for (Player player : forwards) {
            assertEquals("Forward", player.getPosition());
        }
    }

    @Test
    @Order(10)
    @DisplayName("Should search players by age range via GET /api/players/search/age")
    void searchPlayersByAgeRange_ShouldReturnPlayersInRange() {
        // Given
        Player youngPlayer = new Player("Young", "Player", "young@example.com", 20, "Forward");
        playerRepository.save(youngPlayer);

        Player middlePlayer = new Player("Middle", "Player", "middle@example.com", 25, "Midfielder");
        playerRepository.save(middlePlayer);

        Player oldPlayer = new Player("Old", "Player", "old@example.com", 35, "Defender");
        playerRepository.save(oldPlayer);

        // When
        ResponseEntity<Player[]> response = restTemplate.getForEntity(
            baseUrl + "/search/age?minAge=22&maxAge=30", 
            Player[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player[] playersInRange = response.getBody();
        assertEquals(1, playersInRange.length);
        assertEquals("Middle", playersInRange[0].getFirstName());
        assertTrue(playersInRange[0].getAge() >= 22 && playersInRange[0].getAge() <= 30);
    }

    @Test
    @Order(11)
    @DisplayName("Should get active players via GET /api/players/active")
    void getActivePlayers_ShouldReturnOnlyActivePlayers() {
        // Given
        Player activePlayer = new Player("Active", "Player", "active@example.com", 25, "Forward");
        activePlayer.setActive(true);
        playerRepository.save(activePlayer);

        Player inactivePlayer = new Player("Inactive", "Player", "inactive@example.com", 26, "Midfielder");
        inactivePlayer.setActive(false);
        playerRepository.save(inactivePlayer);

        // When
        ResponseEntity<Player[]> response = restTemplate.getForEntity(
            baseUrl + "/active", 
            Player[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player[] activePlayers = response.getBody();
        assertEquals(1, activePlayers.length);
        assertEquals("Active", activePlayers[0].getFirstName());
        assertTrue(activePlayers[0].getActive());
    }

    @Test
    @Order(12)
    @DisplayName("Should deactivate player via PATCH /api/players/{id}/deactivate")
    void deactivatePlayer_ShouldSetActiveToFalse() {
        // Given
        Player activePlayer = new Player("Active", "Player", "active@example.com", 25, "Forward");
        activePlayer.setActive(true);
        Player savedPlayer = playerRepository.save(activePlayer);

        // When
        ResponseEntity<Player> response = restTemplate.exchange(
            baseUrl + "/" + savedPlayer.getId() + "/deactivate",
            HttpMethod.PATCH,
            null,
            Player.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Player deactivatedPlayer = response.getBody();
        assertFalse(deactivatedPlayer.getActive());

        // Verify in database
        Player dbPlayer = playerRepository.findById(savedPlayer.getId()).orElse(null);
        assertNotNull(dbPlayer);
        assertFalse(dbPlayer.getActive());
    }

    @Test
    @Order(13)
    @DisplayName("Should get player count by team via GET /api/players/count/team/{teamName}")
    void getPlayerCountByTeam_ShouldReturnCorrectCount() {
        // Given
        Player player1 = new Player("Player1", "Team", "player1@example.com", 25, "Forward");
        player1.setTeamName("Test Team");
        playerRepository.save(player1);

        Player player2 = new Player("Player2", "Team", "player2@example.com", 26, "Midfielder");
        player2.setTeamName("Test Team");
        playerRepository.save(player2);

        Player player3 = new Player("Player3", "Other", "player3@example.com", 27, "Defender");
        player3.setTeamName("Other Team");
        playerRepository.save(player3);

        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/count/team/Test Team", 
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> responseBody = response.getBody();
        assertEquals("Test Team", responseBody.get("teamName"));
        assertEquals(2, responseBody.get("playerCount"));
    }

    @Test
    @Order(14)
    @DisplayName("Should return health status via GET /api/players/health")
    void healthCheck_ShouldReturnServiceStatus() {
        // Given
        Player player = playerRepository.save(testPlayer);

        // When
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/health", 
            Map.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> health = response.getBody();
        assertEquals("UP", health.get("status"));
        assertEquals("PlayerService", health.get("service"));
        assertTrue((Integer) health.get("totalPlayers") >= 1);
        assertTrue((Integer) health.get("activePlayers") >= 1);
        assertNotNull(health.get("timestamp"));
    }

    @Test
    @Order(15)
    @DisplayName("Should handle concurrent player creation")
    void concurrentPlayerCreation_ShouldHandleGracefully() throws InterruptedException {
        // Given
        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        boolean[] results = new boolean[numberOfThreads];

        // When - Create players concurrently
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    Player player = new Player(
                        "Concurrent" + threadIndex, 
                        "Player", 
                        "concurrent" + threadIndex + "@example.com", 
                        25 + threadIndex, 
                        "Forward"
                    );

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Player> request = new HttpEntity<>(player, headers);

                    ResponseEntity<Player> response = restTemplate.postForEntity(baseUrl, request, Player.class);
                    results[threadIndex] = response.getStatusCode() == HttpStatus.CREATED;
                } catch (Exception e) {
                    results[threadIndex] = false;
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Then - All creations should succeed
        for (boolean result : results) {
            assertTrue(result, "All concurrent player creations should succeed");
        }

        // Verify all players were created
        List<Player> allPlayers = playerRepository.findAll();
        assertEquals(numberOfThreads, allPlayers.size());
    }

    @Test
    @Order(16)
    @DisplayName("Should validate complete CRUD workflow")
    void completeCrudWorkflow_ShouldWorkEndToEnd() {
        // Create
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Player> createRequest = new HttpEntity<>(testPlayer, headers);

        ResponseEntity<Player> createResponse = restTemplate.postForEntity(baseUrl, createRequest, Player.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long playerId = createResponse.getBody().getId();

        // Read
        ResponseEntity<Player> readResponse = restTemplate.getForEntity(
            baseUrl + "/" + playerId, Player.class);
        assertEquals(HttpStatus.OK, readResponse.getStatusCode());
        assertEquals("John", readResponse.getBody().getFirstName());

        // Update
        Player updateData = new Player("Johnny", "Doe", "johnny.doe@example.com", 26, "Midfielder");
        HttpEntity<Player> updateRequest = new HttpEntity<>(updateData, headers);

        ResponseEntity<Player> updateResponse = restTemplate.exchange(
            baseUrl + "/" + playerId, HttpMethod.PUT, updateRequest, Player.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Johnny", updateResponse.getBody().getFirstName());
        assertEquals("Midfielder", updateResponse.getBody().getPosition());

        // Delete
        ResponseEntity<Map> deleteResponse = restTemplate.exchange(
            baseUrl + "/" + playerId, HttpMethod.DELETE, null, Map.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Verify deletion
        ResponseEntity<Map> verifyResponse = restTemplate.getForEntity(
            baseUrl + "/" + playerId, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, verifyResponse.getStatusCode());
    }
}
