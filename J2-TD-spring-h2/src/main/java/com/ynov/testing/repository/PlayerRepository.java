package com.ynov.testing.repository;

import com.ynov.testing.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PlayerRepository Interface
 * 
 * This interface extends JpaRepository to provide CRUD operations for Player entities.
 * Spring Data JPA will automatically implement this interface at runtime.
 * 
 * This repository demonstrates:
 * - Basic CRUD operations (inherited from JpaRepository)
 * - Custom query methods using method naming conventions
 * - Custom JPQL queries using @Query annotation
 * - Various finder methods for testing purposes
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Find a player by email address.
     * This method demonstrates Spring Data JPA's query derivation from method names.
     * 
     * @param email The email address to search for
     * @return Optional containing the player if found, empty otherwise
     */
    Optional<Player> findByEmail(String email);

    /**
     * Find players by first name (case-insensitive).
     * 
     * @param firstName The first name to search for
     * @return List of players with matching first name
     */
    List<Player> findByFirstNameIgnoreCase(String firstName);

    /**
     * Find players by last name (case-insensitive).
     * 
     * @param lastName The last name to search for
     * @return List of players with matching last name
     */
    List<Player> findByLastNameIgnoreCase(String lastName);

    /**
     * Find players by team name.
     * 
     * @param teamName The team name to search for
     * @return List of players in the specified team
     */
    List<Player> findByTeamName(String teamName);

    /**
     * Find players by position.
     * 
     * @param position The position to search for
     * @return List of players with the specified position
     */
    List<Player> findByPosition(String position);

    /**
     * Find players by age range.
     * 
     * @param minAge Minimum age (inclusive)
     * @param maxAge Maximum age (inclusive)
     * @return List of players within the age range
     */
    List<Player> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Find players older than specified age.
     * 
     * @param age The minimum age threshold
     * @return List of players older than the specified age
     */
    List<Player> findByAgeGreaterThan(Integer age);

    /**
     * Find players by active status.
     * 
     * @param active The active status to filter by
     * @return List of players with the specified active status
     */
    List<Player> findByActive(Boolean active);

    /**
     * Find players by team name and position.
     * This demonstrates combining multiple criteria.
     * 
     * @param teamName The team name
     * @param position The position
     * @return List of players matching both criteria
     */
    List<Player> findByTeamNameAndPosition(String teamName, String position);

    /**
     * Find players by team name and active status.
     * 
     * @param teamName The team name
     * @param active The active status
     * @return List of active/inactive players in the specified team
     */
    List<Player> findByTeamNameAndActive(String teamName, Boolean active);

    /**
     * Check if a player exists with the given email.
     * 
     * @param email The email to check
     * @return true if a player with this email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if a player exists with the given jersey number in a specific team.
     * 
     * @param jerseyNumber The jersey number
     * @param teamName The team name
     * @return true if a player with this jersey number exists in the team, false otherwise
     */
    boolean existsByJerseyNumberAndTeamName(Integer jerseyNumber, String teamName);

    /**
     * Count players in a specific team.
     * 
     * @param teamName The team name
     * @return Number of players in the team
     */
    long countByTeamName(String teamName);

    /**
     * Count active players.
     * 
     * @return Number of active players
     */
    long countByActive(Boolean active);

    /**
     * Custom JPQL query to find players by full name (first name + last name).
     * This demonstrates the use of @Query annotation.
     * 
     * @param fullName The full name to search for (case-insensitive)
     * @return List of players matching the full name
     */
    @Query("SELECT p FROM Player p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Player> findByFullNameContaining(@Param("fullName") String fullName);

    /**
     * Custom JPQL query to find players with salary above a certain amount.
     * 
     * @param minSalary The minimum salary threshold
     * @return List of players with salary above the threshold
     */
    @Query("SELECT p FROM Player p WHERE p.salary > :minSalary ORDER BY p.salary DESC")
    List<Player> findPlayersWithSalaryAbove(@Param("minSalary") Double minSalary);

    /**
     * Custom JPQL query to find the highest paid player in each team.
     * This demonstrates a more complex query for advanced testing scenarios.
     * 
     * @return List of players who are the highest paid in their respective teams
     */
    @Query("SELECT p FROM Player p WHERE p.salary = (SELECT MAX(p2.salary) FROM Player p2 WHERE p2.teamName = p.teamName)")
    List<Player> findHighestPaidPlayersByTeam();

    /**
     * Custom native SQL query to get player statistics.
     * This demonstrates the use of native queries.
     * 
     * @return List of objects containing team statistics
     */
    @Query(value = "SELECT team_name, COUNT(*) as player_count, AVG(age) as avg_age, AVG(salary) as avg_salary " +
                   "FROM players WHERE active = true GROUP BY team_name", nativeQuery = true)
    List<Object[]> getTeamStatistics();

    /**
     * Custom update query to deactivate players in a specific team.
     * This demonstrates the use of @Modifying annotation.
     * 
     * @param teamName The team name
     * @return Number of players updated
     */
    @Modifying
    @Query("UPDATE Player p SET p.active = false WHERE p.teamName = :teamName")
    int deactivatePlayersByTeam(@Param("teamName") String teamName);

    /**
     * Delete players by team name.
     * This demonstrates the delete query method naming convention.
     * 
     * @param teamName The team name
     * @return Number of players deleted
     */
    long deleteByTeamName(String teamName);
}
