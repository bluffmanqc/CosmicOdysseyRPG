package com.cosmicodyssey.rpg.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Character {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String race;
    private String className;
    private String background;
    private String alignment;
    private int level;
    private int experience;
    private int hitPoints;
    private int maxHitPoints;
    private int shieldPoints;
    private int maxShieldPoints;
    private int energy;
    private int maxEnergy;
    private int credits;
    private String avatarUrl;
    private String avatarType;
    private String description;
    @Ignore
    private CharacterStats stats;

    @Ignore
    private List<Equipment> equipments;
    @Ignore
    private Mount mount;
    @Ignore
    private Spaceship spaceship;
    @Ignore
    private Cargo cargo;
    private String currentPlanet;
    private String currentSystem;

    @Ignore
    private List<String> knownSystems;

    @Ignore
    private List<String> visitedPlanets;
    private String partyId;
    private long createdAt;
    private long updatedAt;

    public Character() {
        this.id = UUID.randomUUID().toString();
        this.level = 1;
        this.experience = 0;
        this.hitPoints = 20;
        this.maxHitPoints = 20;
        this.shieldPoints = 10;
        this.maxShieldPoints = 10;
        this.energy = 50;
        this.maxEnergy = 50;
        this.credits = 100;
        this.stats = new CharacterStats();
        this.equipments = new ArrayList<>();
        this.knownSystems = new ArrayList<>();
        this.visitedPlanets = new ArrayList<>();
        this.avatarType = "ai";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getBackground() { return background; }
    public void setBackground(String background) { this.background = background; }
    public String getAlignment() { return alignment; }
    public void setAlignment(String alignment) { this.alignment = alignment; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getHitPoints() { return hitPoints; }
    public void setHitPoints(int hitPoints) { this.hitPoints = hitPoints; }
    public int getMaxHitPoints() { return maxHitPoints; }
    public void setMaxHitPoints(int maxHitPoints) { this.maxHitPoints = maxHitPoints; }
    public int getShieldPoints() { return shieldPoints; }
    public void setShieldPoints(int shieldPoints) { this.shieldPoints = shieldPoints; }
    public int getMaxShieldPoints() { return maxShieldPoints; }
    public void setMaxShieldPoints(int maxShieldPoints) { this.maxShieldPoints = maxShieldPoints; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public void setMaxEnergy(int maxEnergy) { this.maxEnergy = maxEnergy; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getAvatarType() { return avatarType; }
    public void setAvatarType(String avatarType) { this.avatarType = avatarType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CharacterStats getStats() { return stats; }
    public void setStats(CharacterStats stats) { this.stats = stats; }
    public List<Equipment> getEquipments() { return equipments; }
    public void setEquipments(List<Equipment> equipments) { this.equipments = equipments; }
    public Mount getMount() { return mount; }
    public void setMount(Mount mount) { this.mount = mount; }
    public Spaceship getSpaceship() { return spaceship; }
    public void setSpaceship(Spaceship spaceship) { this.spaceship = spaceship; }
    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public String getCurrentPlanet() { return currentPlanet; }
    public void setCurrentPlanet(String currentPlanet) { this.currentPlanet = currentPlanet; }
    public String getCurrentSystem() { return currentSystem; }
    public void setCurrentSystem(String currentSystem) { this.currentSystem = currentSystem; }
    public List<String> getKnownSystems() { return knownSystems; }
    public void setKnownSystems(List<String> knownSystems) { this.knownSystems = knownSystems; }
    public List<String> getVisitedPlanets() { return visitedPlanets; }
    public void setVisitedPlanets(List<String> visitedPlanets) { this.visitedPlanets = visitedPlanets; }
    public String getPartyId() { return partyId; }
    public void setPartyId(String partyId) { this.partyId = partyId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
        updatedAt = System.currentTimeMillis();
    }

    public void removeEquipment(Equipment equipment) {
        equipments.remove(equipment);
        updatedAt = System.currentTimeMillis();
    }

    public String getCharacterPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personnage: ").append(name).append("\n");
        sb.append("Race: ").append(race).append(" | Classe: ").append(className).append(" | Niveau: ").append(level).append("\n");
        sb.append("Équipements: ");
        for (Equipment e : equipments) {
            sb.append(e.getName()).append(", ");
        }
        if (mount != null) sb.append("Monture: ").append(mount.getName()).append(", ");
        if (spaceship != null) sb.append("Vaisseau: ").append(spaceship.getName()).append(", ");
        return sb.toString();
    }
}
