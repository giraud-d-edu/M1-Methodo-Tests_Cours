package com.ynov.testing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Player Entity
 * 
 * This class represents a Player in our system.
 * It demonstrates the use of JPA annotations for database mapping
 * and validation annotations for input validation.
 * 
 * This entity is perfect for testing purposes as it contains:
 * - Different data types (String, Integer, LocalDateTime, etc.)
 * - Validation constraints
 * - Relationships possibilities for future extensions
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotNull(message = "Age is mandatory")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank(message = "Position is mandatory")
    @Size(min = 2, max = 30, message = "Position must be between 2 and 30 characters")
    @Column(name = "position", nullable = false, length = 30)
    private String position;

    @Column(name = "team_name", length = 50)
    private String teamName;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor (required by JPA)
    public Player() {
    }

    // Constructor with essential fields
    public Player(String firstName, String lastName, String email, Integer age, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.position = position;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with all fields except id and timestamps
    public Player(String firstName, String lastName, String email, Integer age, String position, 
                  String teamName, Integer jerseyNumber, Double salary, Boolean active) {
        this(firstName, lastName, email, age, position);
        this.teamName = teamName;
        this.jerseyNumber = jerseyNumber;
        this.salary = salary;
        this.active = active != null ? active : true;
    }

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // equals() and hashCode() methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && 
               Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    // toString() method
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", teamName='" + teamName + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", salary=" + salary +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
