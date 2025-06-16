package com.ynov.testing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Team Entity representing a League of Legends team
 *
 * A League of Legends team consists of 5 players with specific roles:
 * - Top Laner
 * - Jungler
 * - Mid Laner
 * - ADC (Attack Damage Carry)
 * - Support
 *
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 50, message = "Team name must be between 2 and 50 characters")
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank(message = "Region is required")
    @Size(max = 10, message = "Region code must not exceed 10 characters")
    private String region;

    @NotNull(message = "Founded date is required")
    private LocalDateTime foundedDate;

    @Email(message = "Invalid email format")
    private String contactEmail;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    private Double budget;

    @Min(value = 0, message = "Wins cannot be negative")
    private Integer wins = 0;

    @Min(value = 0, message = "Losses cannot be negative")
    private Integer losses = 0;

    @Column(name = "is_active")
    private Boolean active = true;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Player> players = new ArrayList<>();

    // Constructeurs
    public Team() {}

    public Team(String name, String region, LocalDateTime foundedDate) {
        this.name = name;
        this.region = region;
        this.foundedDate = foundedDate;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public LocalDateTime getFoundedDate() { return foundedDate; }
    public void setFoundedDate(LocalDateTime foundedDate) { this.foundedDate = foundedDate; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public Integer getWins() { return wins; }
    public void setWins(Integer wins) { this.wins = wins; }

    public Integer getLosses() { return losses; }
    public void setLosses(Integer losses) { this.losses = losses; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    // Méthodes utilitaires
    public Double getWinRate() {
        if (wins + losses == 0) return 0.0;
        return (double) wins / (wins + losses) * 100;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setTeamName(this.name);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setTeamName(null);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", wins=" + wins +
                ", losses=" + losses +
                ", winRate=" + getWinRate() + "%" +
                '}';
    }
}
