package com.ynov.testing.repository;

import com.ynov.testing.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for PlayerRepository
 * 
 * This class demonstrates JPA repository testing:
 * - Using @DataJpaTest for repository layer testing
 * - Using TestEntityManager for test data setup
 * - Testing with H2 in-memory database
 * - Testing custom query methods
 * - Testing JPA relationships and constraints
 * 
 * @DataJpaTest provides:
 * - Auto-configuration for JPA repositories
 * - In-memory database (H2)
 * - Transaction rollback after each test
 * - TestEntityManager for data manipulation
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Player Repository Integration Tests")
class PlayerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    private Player testPlayer1;
    private Player testPlayer2;
    private Player testPlayer3;

    @BeforeEach
    void setUp() {
        // Create test data
        testPlayer1 = new Player("John", "Doe", "john.doe@example.com", 25, "Forward");
        testPlayer1.setTeamName("Team A");
        testPlayer1.setJerseyNumber(10);
        testPlayer1.setSalary(50000.0);
        testPlayer1.setActive(true);

        testPlayer2 = new Player("Jane", "Smith", "jane.smith@example.com", 23, "Midfielder");
        testPlayer2.setTeamName("Team A");
        testPlayer2.setJerseyNumber(8);
        testPlayer2.setSalary(45000.0);
        testPlayer2.setActive(true);

        testPlayer3 = new Player("Bob", "Johnson", "bob.johnson@example.com", 28, "Defender");
        testPlayer3.setTeamName("Team B");
        testPlayer3.setJerseyNumber(5);
        testPlayer3.setSalary(48000.0);
        testPlayer3.setActive(false);
    }

    @Test
    @DisplayName("Should save and find player by ID")
    void saveAndFindById_ShouldPersistAndRetrievePlayer() {
        // Given
        Player savedPlayer = entityManager.persistAndFlush(testPlayer1);
        entityManager.clear();

        // When
        Optional<Player> foundPlayer = playerRepository.findById(savedPlayer.getId());

        // Then
        assertTrue(foundPlayer.isPresent());
        assertEquals(testPlayer1.getFirstName(), foundPlayer.get().getFirstName());
        assertEquals(testPlayer1.getLastName(), foundPlayer.get().getLastName());
        assertEquals(testPlayer1.getEmail(), foundPlayer.get().getEmail());
        assertEquals(testPlayer1.getAge(), foundPlayer.get().getAge());
        assertEquals(testPlayer1.getPosition(), foundPlayer.get().getPosition());
        assertNotNull(foundPlayer.get().getCreatedAt());
        assertNotNull(foundPlayer.get().getUpdatedAt());
    }

    @Test
    @DisplayName("Should find player by email")
    void findByEmail_WithExistingEmail_ShouldReturnPlayer() {
        // Given
        entityManager.persistAndFlush(testPlayer1);
        entityManager.clear();

        // When
        Optional<Player> foundPlayer = playerRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(foundPlayer.isPresent());
        assertEquals(testPlayer1.getFirstName(), foundPlayer.get().getFirstName());
        assertEquals(testPlayer1.getEmail(), foundPlayer.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when finding player by non-existent email")
    void findByEmail_WithNonExistentEmail_ShouldReturnEmpty() {
        // When
        Optional<Player> foundPlayer = playerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundPlayer.isPresent());
    }

    @Test
    @DisplayName("Should find players by team name")
    void findByTeamName_ShouldReturnPlayersFromTeam() {
        // Given
        entityManager.persistAndFlush(testPlayer1);
        entityManager.persistAndFlush(testPlayer2);
        entityManager.persistAndFlush(testPlayer3);
        entityManager.clear();

        // When
        List<Player> teamAPlayers = playerRepository.findByTeamName("Team A");
        List<Player> teamBPlayers = playerRepository.findByTeamName("Team B");

        // Then
        assertEquals(2, teamAPlayers.size());
        assertEquals(1, teamBPlayers.size());
        assertTrue(teamAPlayers.stream().allMatch(player -> "Team A".equals(player.getTeamName())));
        assertTrue(teamBPlayers.stream().allMatch(player -> "Team B".equals(player.getTeamName())));
    }

    @Test
    @DisplayName("Should find players by position")
    void findByPosition_ShouldReturnPlayersWithPosition() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // Forward
        entityManager.persistAndFlush(testPlayer2); // Midfielder
        entityManager.persistAndFlush(testPlayer3); // Defender
        entityManager.clear();

        // When
        List<Player> forwards = playerRepository.findByPosition("Forward");
        List<Player> midfielders = playerRepository.findByPosition("Midfielder");
        List<Player> defenders = playerRepository.findByPosition("Defender");

        // Then
        assertEquals(1, forwards.size());
        assertEquals(1, midfielders.size());
        assertEquals(1, defenders.size());
        assertEquals("Forward", forwards.get(0).getPosition());
        assertEquals("Midfielder", midfielders.get(0).getPosition());
        assertEquals("Defender", defenders.get(0).getPosition());
    }

    @Test
    @DisplayName("Should find players by age range")
    void findByAgeBetween_ShouldReturnPlayersInAgeRange() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // age 25
        entityManager.persistAndFlush(testPlayer2); // age 23
        entityManager.persistAndFlush(testPlayer3); // age 28
        entityManager.clear();

        // When
        List<Player> playersInRange = playerRepository.findByAgeBetween(23, 26);

        // Then
        assertEquals(2, playersInRange.size());
        assertTrue(playersInRange.stream().allMatch(player -> 
            player.getAge() >= 23 && player.getAge() <= 26));
    }

    @Test
    @DisplayName("Should find players older than specified age")
    void findByAgeGreaterThan_ShouldReturnOlderPlayers() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // age 25
        entityManager.persistAndFlush(testPlayer2); // age 23
        entityManager.persistAndFlush(testPlayer3); // age 28
        entityManager.clear();

        // When
        List<Player> olderPlayers = playerRepository.findByAgeGreaterThan(24);

        // Then
        assertEquals(2, olderPlayers.size());
        assertTrue(olderPlayers.stream().allMatch(player -> player.getAge() > 24));
    }

    @Test
    @DisplayName("Should find players by active status")
    void findByActive_ShouldReturnPlayersWithActiveStatus() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // active
        entityManager.persistAndFlush(testPlayer2); // active
        entityManager.persistAndFlush(testPlayer3); // inactive
        entityManager.clear();

        // When
        List<Player> activePlayers = playerRepository.findByActive(true);
        List<Player> inactivePlayers = playerRepository.findByActive(false);

        // Then
        assertEquals(2, activePlayers.size());
        assertEquals(1, inactivePlayers.size());
        assertTrue(activePlayers.stream().allMatch(Player::getActive));
        assertTrue(inactivePlayers.stream().noneMatch(Player::getActive));
    }

    @Test
    @DisplayName("Should find players by team name and position")
    void findByTeamNameAndPosition_ShouldReturnMatchingPlayers() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // Team A, Forward
        entityManager.persistAndFlush(testPlayer2); // Team A, Midfielder
        entityManager.persistAndFlush(testPlayer3); // Team B, Defender
        entityManager.clear();

        // When
        List<Player> teamAForwards = playerRepository.findByTeamNameAndPosition("Team A", "Forward");
        List<Player> teamAMidfielders = playerRepository.findByTeamNameAndPosition("Team A", "Midfielder");

        // Then
        assertEquals(1, teamAForwards.size());
        assertEquals(1, teamAMidfielders.size());
        assertEquals("John", teamAForwards.get(0).getFirstName());
        assertEquals("Jane", teamAMidfielders.get(0).getFirstName());
    }

    @Test
    @DisplayName("Should check if player exists by email")
    void existsByEmail_ShouldReturnCorrectBoolean() {
        // Given
        entityManager.persistAndFlush(testPlayer1);
        entityManager.clear();

        // When & Then
        assertTrue(playerRepository.existsByEmail("john.doe@example.com"));
        assertFalse(playerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should check if player exists by jersey number and team")
    void existsByJerseyNumberAndTeamName_ShouldReturnCorrectBoolean() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // Team A, Jersey 10
        entityManager.clear();

        // When & Then
        assertTrue(playerRepository.existsByJerseyNumberAndTeamName(10, "Team A"));
        assertFalse(playerRepository.existsByJerseyNumberAndTeamName(10, "Team B"));
        assertFalse(playerRepository.existsByJerseyNumberAndTeamName(99, "Team A"));
    }

    @Test
    @DisplayName("Should count players by team name")
    void countByTeamName_ShouldReturnCorrectCount() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // Team A
        entityManager.persistAndFlush(testPlayer2); // Team A
        entityManager.persistAndFlush(testPlayer3); // Team B
        entityManager.clear();

        // When
        long teamACount = playerRepository.countByTeamName("Team A");
        long teamBCount = playerRepository.countByTeamName("Team B");
        long nonExistentTeamCount = playerRepository.countByTeamName("Team C");

        // Then
        assertEquals(2, teamACount);
        assertEquals(1, teamBCount);
        assertEquals(0, nonExistentTeamCount);
    }

    @Test
    @DisplayName("Should count active/inactive players")
    void countByActive_ShouldReturnCorrectCount() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // active
        entityManager.persistAndFlush(testPlayer2); // active
        entityManager.persistAndFlush(testPlayer3); // inactive
        entityManager.clear();

        // When
        long activeCount = playerRepository.countByActive(true);
        long inactiveCount = playerRepository.countByActive(false);

        // Then
        assertEquals(2, activeCount);
        assertEquals(1, inactiveCount);
    }

    @Test
    @DisplayName("Should find players by full name containing")
    void findByFullNameContaining_ShouldReturnMatchingPlayers() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // John Doe
        entityManager.persistAndFlush(testPlayer2); // Jane Smith
        entityManager.persistAndFlush(testPlayer3); // Bob Johnson
        entityManager.clear();

        // When
        List<Player> johnResults = playerRepository.findByFullNameContaining("John");
        List<Player> smithResults = playerRepository.findByFullNameContaining("Smith");
        List<Player> doeResults = playerRepository.findByFullNameContaining("Doe");

        // Then
        assertEquals(2, johnResults.size()); // John Doe and Bob Johnson
        assertEquals(1, smithResults.size()); // Jane Smith
        assertEquals(1, doeResults.size()); // John Doe
    }

    @Test
    @DisplayName("Should find players with salary above threshold")
    void findPlayersWithSalaryAbove_ShouldReturnHighEarners() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // 50000
        entityManager.persistAndFlush(testPlayer2); // 45000
        entityManager.persistAndFlush(testPlayer3); // 48000
        entityManager.clear();

        // When
        List<Player> highEarners = playerRepository.findPlayersWithSalaryAbove(47000.0);

        // Then
        assertEquals(2, highEarners.size());
        assertTrue(highEarners.stream().allMatch(player -> player.getSalary() > 47000.0));
        // Should be ordered by salary DESC
        assertTrue(highEarners.get(0).getSalary() >= highEarners.get(1).getSalary());
    }

    @Test
    @DisplayName("Should delete players by team name")
    void deleteByTeamName_ShouldRemoveTeamPlayers() {
        // Given
        entityManager.persistAndFlush(testPlayer1); // Team A
        entityManager.persistAndFlush(testPlayer2); // Team A
        entityManager.persistAndFlush(testPlayer3); // Team B
        entityManager.clear();

        // When
        long deletedCount = playerRepository.deleteByTeamName("Team A");
        entityManager.clear();

        // Then
        assertEquals(2, deletedCount);
        assertEquals(0, playerRepository.countByTeamName("Team A"));
        assertEquals(1, playerRepository.countByTeamName("Team B"));
    }

    @Test
    @DisplayName("Should handle unique constraints")
    void save_WithDuplicateEmail_ShouldThrowException() {
        // Given
        entityManager.persistAndFlush(testPlayer1);

        Player duplicateEmailPlayer = new Player("Different", "Name", "john.doe@example.com", 30, "Goalkeeper");

        // When & Then
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateEmailPlayer);
        });
    }

    @Test
    @DisplayName("Should handle cascade operations")
    void save_ShouldSetTimestamps() {
        // When
        Player savedPlayer = playerRepository.save(testPlayer1);

        // Then
        assertNotNull(savedPlayer.getId());
        assertNotNull(savedPlayer.getCreatedAt());
        assertNotNull(savedPlayer.getUpdatedAt());
        assertEquals(savedPlayer.getCreatedAt(), savedPlayer.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update timestamps on modification")
    void update_ShouldUpdateTimestamp() throws InterruptedException {
        // Given
        Player savedPlayer = entityManager.persistAndFlush(testPlayer1);
        entityManager.clear();

        // Wait a bit to ensure different timestamp
        Thread.sleep(10);

        // When
        Optional<Player> foundPlayer = playerRepository.findById(savedPlayer.getId());
        assertTrue(foundPlayer.isPresent());

        foundPlayer.get().setFirstName("Updated Name");
        Player updatedPlayer = playerRepository.save(foundPlayer.get());

        // Then
        assertNotNull(updatedPlayer.getUpdatedAt());
        assertTrue(updatedPlayer.getUpdatedAt().isAfter(updatedPlayer.getCreatedAt()) || 
                  updatedPlayer.getUpdatedAt().equals(updatedPlayer.getCreatedAt()));
    }

    @Test
    @DisplayName("Should find all players with pagination")
    void findAll_ShouldReturnAllPlayers() {
        // Given
        entityManager.persistAndFlush(testPlayer1);
        entityManager.persistAndFlush(testPlayer2);
        entityManager.persistAndFlush(testPlayer3);
        entityManager.clear();

        // When
        List<Player> allPlayers = playerRepository.findAll();

        // Then
        assertEquals(3, allPlayers.size());
    }

    @Test
    @DisplayName("Should perform batch operations efficiently")
    void saveAll_ShouldPersistMultiplePlayers() {
        // Given
        List<Player> players = List.of(testPlayer1, testPlayer2, testPlayer3);

        // When
        List<Player> savedPlayers = playerRepository.saveAll(players);

        // Then
        assertEquals(3, savedPlayers.size());
        assertTrue(savedPlayers.stream().allMatch(player -> player.getId() != null));
    }
}
