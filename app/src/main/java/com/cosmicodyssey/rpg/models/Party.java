package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.TypeConverters;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@TypeConverters({com.cosmicodyssey.rpg.data.StringListConverter.class})
public class Party {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String description;
    private String gameMasterType;
    private String difficulty;

    @Ignore
    private List<String> playerIds;
    private String hostId;
    private String currentPlanet;
    private String currentSystem;
    private String storyPrompt;

        private List<String> storyHistory;

    @Ignore
    private List<Equipment> sharedEquipment;

    @Ignore
    private List<Mount> sharedMounts;

    @Ignore
    private List<Spaceship> sharedSpaceships;

    @Ignore
    private List<Cargo> sharedCargo;

    @Ignore
    private List<Planet> discoveredPlanets;

    @Ignore
    private List<StarSystem> discoveredSystems;
    private boolean isActive;
    private long createdAt;
    private long lastActivity;
    private String partyCode;

    public Party() {
        this.id = UUID.randomUUID().toString();
        this.playerIds = new ArrayList<>();
        this.storyHistory = new ArrayList<>();
        this.sharedEquipment = new ArrayList<>();
        this.sharedMounts = new ArrayList<>();
        this.sharedSpaceships = new ArrayList<>();
        this.sharedCargo = new ArrayList<>();
        this.discoveredPlanets = new ArrayList<>();
        this.discoveredSystems = new ArrayList<>();
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.lastActivity = System.currentTimeMillis();
        this.partyCode = generatePartyCode();
    }

    private String generatePartyCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return sb.toString();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGameMasterType() { return gameMasterType; }
    public void setGameMasterType(String gameMasterType) { this.gameMasterType = gameMasterType; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<String> getPlayerIds() { return playerIds; }
    public void setPlayerIds(List<String> playerIds) { this.playerIds = playerIds; }
    public String getHostId() { return hostId; }
    public void setHostId(String hostId) { this.hostId = hostId; }
    public String getCurrentPlanet() { return currentPlanet; }
    public void setCurrentPlanet(String currentPlanet) { this.currentPlanet = currentPlanet; }
    public String getCurrentSystem() { return currentSystem; }
    public void setCurrentSystem(String currentSystem) { this.currentSystem = currentSystem; }
    public String getStoryPrompt() { return storyPrompt; }
    public void setStoryPrompt(String storyPrompt) { this.storyPrompt = storyPrompt; }
    public List<String> getStoryHistory() { return storyHistory; }
    public void setStoryHistory(List<String> storyHistory) { this.storyHistory = storyHistory; }
    public List<Equipment> getSharedEquipment() { return sharedEquipment; }
    public void setSharedEquipment(List<Equipment> sharedEquipment) { this.sharedEquipment = sharedEquipment; }
    public List<Mount> getSharedMounts() { return sharedMounts; }
    public void setSharedMounts(List<Mount> sharedMounts) { this.sharedMounts = sharedMounts; }
    public List<Spaceship> getSharedSpaceships() { return sharedSpaceships; }
    public void setSharedSpaceships(List<Spaceship> sharedSpaceships) { this.sharedSpaceships = sharedSpaceships; }
    public List<Cargo> getSharedCargo() { return sharedCargo; }
    public void setSharedCargo(List<Cargo> sharedCargo) { this.sharedCargo = sharedCargo; }
    public List<Planet> getDiscoveredPlanets() { return discoveredPlanets; }
    public void setDiscoveredPlanets(List<Planet> discoveredPlanets) { this.discoveredPlanets = discoveredPlanets; }
    public List<StarSystem> getDiscoveredSystems() { return discoveredSystems; }
    public void setDiscoveredSystems(List<StarSystem> discoveredSystems) { this.discoveredSystems = discoveredSystems; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getLastActivity() { return lastActivity; }
    public void setLastActivity(long lastActivity) { this.lastActivity = lastActivity; }
    public String getPartyCode() { return partyCode; }
    public void setPartyCode(String partyCode) { this.partyCode = partyCode; }
}
