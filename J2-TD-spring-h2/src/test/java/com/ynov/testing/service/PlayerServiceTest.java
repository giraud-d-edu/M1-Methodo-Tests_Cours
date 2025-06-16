package com.ynov.testing.service;

import com.ynov.testing.model.Player;
import com.ynov.testing.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for PlayerService
 * 
 * This class demonstrates various JUnit 5 testing techniques:
 * - Using @ExtendWith(MockitoExtension.class) for Mockito integration
 * - Mocking dependencies with @Mock
 * - Injecting mocks with @InjectMocks
 * - Using @DisplayName for readable test names
 * - Testing different scenarios (happy path, edge cases, exceptions)
 * - Verification of mock interactions
 * 
 * These tests focus on testing the service layer logic in isolation
 * by mocking the repository dependency.
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Player Service Unit Tests")
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player testPlayer;
    private Player savedPlayer;

    @BeforeEach
    void setUp() {
        // Create test data before each test
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
    @DisplayName("Should create player successfully when valid data is provided")
    void createPlayer_WithValidData_ShouldReturnSavedPlayer() {
        // Given
        when(playerRepository.existsByEmail(testPlayer.getEmail())).thenReturn(false);
        when(playerRepository.existsByJerseyNumberAndTeamName(testPlayer.getJerseyNumber(), testPlayer.getTeamName()))
            .thenReturn(false);
        when(playerRepository.save(testPlayer)).thenReturn(savedPlayer);

        // When
        Player result = playerService.createPlayer(testPlayer);

        // Then
        assertNotNull(result);
        assertEquals(savedPlayer.getId(), result.getId());
        assertEquals(testPlayer.getFirstName(), result.getFirstName());
        assertEquals(testPlayer.getEmail(), result.getEmail());
        assertTrue(result.getActive());

        // Verify mock interactions
        verify(playerRepository).existsByEmail(testPlayer.getEmail());
        verify(playerRepository).existsByJerseyNumberAndTeamName(testPlayer.getJerseyNumber(), testPlayer.getTeamName());
        verify(playerRepository).save(testPlayer);
    }

    @Test
    @DisplayName("Should throw exception when trying to create player with existing email")
    void createPlayer_WithExistingEmail_ShouldThrowException() {
        // Given
        when(playerRepository.existsByEmail(testPlayer.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.createPlayer(testPlayer)
        );

        assertEquals("Player with email john.doe@example.com already exists", exception.getMessage());
        verify(playerRepository).existsByEmail(testPlayer.getEmail());
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create player with existing jersey number in same team")
    void createPlayer_WithExistingJerseyNumberInTeam_ShouldThrowException() {
        // Given
        when(playerRepository.existsByEmail(testPlayer.getEmail())).thenReturn(false);
        when(playerRepository.existsByJerseyNumberAndTeamName(testPlayer.getJerseyNumber(), testPlayer.getTeamName()))
            .thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.createPlayer(testPlayer)
        );

        assertEquals("Jersey number 10 is already taken in team Test Team", exception.getMessage());
        verify(playerRepository).existsByEmail(testPlayer.getEmail());
        verify(playerRepository).existsByJerseyNumberAndTeamName(testPlayer.getJerseyNumber(), testPlayer.getTeamName());
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create null player")
    void createPlayer_WithNullPlayer_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.createPlayer(null)
        );

        assertEquals("Player cannot be null", exception.getMessage());
        verify(playerRepository, never()).existsByEmail(anyString());
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    @DisplayName("Should return all players when getAllPlayers is called")
    void getAllPlayers_ShouldReturnAllPlayers() {
        // Given
        List<Player> expectedPlayers = Arrays.asList(savedPlayer, 
            new Player("Jane", "Smith", "jane.smith@example.com", 23, "Midfielder"));
        when(playerRepository.findAll()).thenReturn(expectedPlayers);

        // When
        List<Player> result = playerService.getAllPlayers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPlayers, result);
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Should return player when found by valid ID")
    void getPlayerById_WithValidId_ShouldReturnPlayer() {
        // Given
        Long playerId = 1L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));

        // When
        Optional<Player> result = playerService.getPlayerById(playerId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedPlayer, result.get());
        verify(playerRepository).findById(playerId);
    }

    @Test
    @DisplayName("Should return empty when player not found by ID")
    void getPlayerById_WithNonExistentId_ShouldReturnEmpty() {
        // Given
        Long playerId = 999L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Optional<Player> result = playerService.getPlayerById(playerId);

        // Then
        assertFalse(result.isPresent());
        verify(playerRepository).findById(playerId);
    }

    @Test
    @DisplayName("Should throw exception when player ID is null or invalid")
    void getPlayerById_WithInvalidId_ShouldThrowException() {
        // Test null ID
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayerById(null)
        );
        assertEquals("Player ID must be positive", exception1.getMessage());

        // Test zero ID
        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayerById(0L)
        );
        assertEquals("Player ID must be positive", exception2.getMessage());

        // Test negative ID
        IllegalArgumentException exception3 = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayerById(-1L)
        );
        assertEquals("Player ID must be positive", exception3.getMessage());

        verify(playerRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should update player successfully when valid data is provided")
    void updatePlayer_WithValidData_ShouldReturnUpdatedPlayer() {
        // Given
        Long playerId = 1L;
        Player updateData = new Player("John", "Smith", "john.smith@example.com", 26, "Defender");
        updateData.setSalary(60000.0);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));
        when(playerRepository.existsByEmail(updateData.getEmail())).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        // When
        Player result = playerService.updatePlayer(playerId, updateData);

        // Then
        assertNotNull(result);
        verify(playerRepository).findById(playerId);
        verify(playerRepository).existsByEmail(updateData.getEmail());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to update non-existent player")
    void updatePlayer_WithNonExistentId_ShouldThrowException() {
        // Given
        Long playerId = 999L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.updatePlayer(playerId, testPlayer)
        );

        assertEquals("Player not found with ID: 999", exception.getMessage());
        verify(playerRepository).findById(playerId);
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    @DisplayName("Should delete player successfully when player exists")
    void deletePlayer_WithExistingId_ShouldDeletePlayer() {
        // Given
        Long playerId = 1L;
        when(playerRepository.existsById(playerId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> playerService.deletePlayer(playerId));

        // Then
        verify(playerRepository).existsById(playerId);
        verify(playerRepository).deleteById(playerId);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete non-existent player")
    void deletePlayer_WithNonExistentId_ShouldThrowException() {
        // Given
        Long playerId = 999L;
        when(playerRepository.existsById(playerId)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.deletePlayer(playerId)
        );

        assertEquals("Player not found with ID: 999", exception.getMessage());
        verify(playerRepository).existsById(playerId);
        verify(playerRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return players by team name")
    void getPlayersByTeam_WithValidTeamName_ShouldReturnPlayers() {
        // Given
        String teamName = "Test Team";
        List<Player> expectedPlayers = Arrays.asList(savedPlayer);
        when(playerRepository.findByTeamName(teamName)).thenReturn(expectedPlayers);

        // When
        List<Player> result = playerService.getPlayersByTeam(teamName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedPlayers, result);
        verify(playerRepository).findByTeamName(teamName);
    }

    @Test
    @DisplayName("Should throw exception when team name is null or empty")
    void getPlayersByTeam_WithInvalidTeamName_ShouldThrowException() {
        // Test null team name
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayersByTeam(null)
        );
        assertEquals("Team name cannot be null or empty", exception1.getMessage());

        // Test empty team name
        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayersByTeam("")
        );
        assertEquals("Team name cannot be null or empty", exception2.getMessage());

        verify(playerRepository, never()).findByTeamName(any());
    }

    @Test
    @DisplayName("Should return players by age range")
    void getPlayersByAgeRange_WithValidRange_ShouldReturnPlayers() {
        // Given
        Integer minAge = 20;
        Integer maxAge = 30;
        List<Player> expectedPlayers = Arrays.asList(savedPlayer);
        when(playerRepository.findByAgeBetween(minAge, maxAge)).thenReturn(expectedPlayers);

        // When
        List<Player> result = playerService.getPlayersByAgeRange(minAge, maxAge);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedPlayers, result);
        verify(playerRepository).findByAgeBetween(minAge, maxAge);
    }

    @Test
    @DisplayName("Should throw exception when age range is invalid")
    void getPlayersByAgeRange_WithInvalidRange_ShouldThrowException() {
        // Test min age greater than max age
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> playerService.getPlayersByAgeRange(30, 20)
        );
        assertEquals("Minimum age cannot be greater than maximum age", exception.getMessage());

        verify(playerRepository, never()).findByAgeBetween(any(), any());
    }

    @Test
    @DisplayName("Should return active players")
    void getActivePlayers_ShouldReturnActivePlayers() {
        // Given
        List<Player> expectedPlayers = Arrays.asList(savedPlayer);
        when(playerRepository.findByActive(true)).thenReturn(expectedPlayers);

        // When
        List<Player> result = playerService.getActivePlayers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedPlayers, result);
        verify(playerRepository).findByActive(true);
    }

    @Test
    @DisplayName("Should deactivate player successfully")
    void deactivatePlayer_WithValidId_ShouldDeactivatePlayer() {
        // Given
        Long playerId = 1L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(savedPlayer);

        // When
        Player result = playerService.deactivatePlayer(playerId);

        // Then
        assertNotNull(result);
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should count players by team")
    void countPlayersByTeam_WithValidTeamName_ShouldReturnCount() {
        // Given
        String teamName = "Test Team";
        long expectedCount = 5L;
        when(playerRepository.countByTeamName(teamName)).thenReturn(expectedCount);

        // When
        long result = playerService.countPlayersByTeam(teamName);

        // Then
        assertEquals(expectedCount, result);
        verify(playerRepository).countByTeamName(teamName);
    }

    @Test
    @DisplayName("Should count active players")
    void countActivePlayers_ShouldReturnActiveCount() {
        // Given
        long expectedCount = 10L;
        when(playerRepository.countByActive(true)).thenReturn(expectedCount);

        // When
        long result = playerService.countActivePlayers();

        // Then
        assertEquals(expectedCount, result);
        verify(playerRepository).countByActive(true);
    }

    @Test
    @DisplayName("Should calculate average age correctly")
    void calculateAverageAgeByTeam_WithPlayersInTeam_ShouldReturnAverageAge() {
        // Given
        String teamName = "Test Team";
        List<Player> teamPlayers = Arrays.asList(
            new Player("John", "Doe", "john@example.com", 25, "Forward"),
            new Player("Jane", "Smith", "jane@example.com", 27, "Midfielder"),
            new Player("Bob", "Johnson", "bob@example.com", 23, "Defender")
        );
        when(playerRepository.findByTeamName(teamName)).thenReturn(teamPlayers);

        // When
        Double result = playerService.calculateAverageAgeByTeam(teamName);

        // Then
        assertEquals(25.0, result); // (25 + 27 + 23) / 3 = 25.0
        verify(playerRepository).findByTeamName(teamName);
    }

    @Test
    @DisplayName("Should return zero when no players in team")
    void calculateAverageAgeByTeam_WithNoPlayersInTeam_ShouldReturnZero() {
        // Given
        String teamName = "Empty Team";
        when(playerRepository.findByTeamName(teamName)).thenReturn(Arrays.asList());

        // When
        Double result = playerService.calculateAverageAgeByTeam(teamName);

        // Then
        assertEquals(0.0, result);
        verify(playerRepository).findByTeamName(teamName);
    }
}
