package com.ynov.testing.model;

/**
 * Enumeration of League of Legends player roles
 *
 * Each team must have exactly one player for each role:
 * - TOP: Solo lane, usually tanks or fighters
 * - JUNGLE: Map control, ganks, and objective control
 * - MID: Usually mages or assassins with roam potential
 * - ADC: Attack Damage Carry, ranged champions focused on scaling
 * - SUPPORT: Vision control, team utility, and ADC protection
 */
public enum PlayerRole {
    TOP("Top Laner", "Solo lane fighter or tank"),
    JUNGLE("Jungler", "Map controller and ganker"),
    MID("Mid Laner", "Magic damage dealer or assassin"),
    ADC("Attack Damage Carry", "Ranged physical damage dealer"),
    SUPPORT("Support", "Team utility and vision control");

    private final String displayName;
    private final String description;

    PlayerRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}
