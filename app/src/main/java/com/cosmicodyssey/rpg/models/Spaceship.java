package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.UUID;

@Entity
public class Spaceship {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String model;
    private String description;
    @Ignore
    private Rarity rarity;
    private int levelRequired;
    private int speed;
    private int hull;
    private int maxHull;
    private int shields;
    private int maxShields;
    private int weaponSlots;
    private int cargoCapacity;
    private int crewCapacity;
    private int jumpRange;
    private String imageUrl;
    private String lore;
    private String origin;
    private String creatorId;
    private long createdAt;
    private boolean owned;

    public Spaceship() {
        this.id = UUID.randomUUID().toString();
        this.rarity = Rarity.COMMON;
        this.createdAt = System.currentTimeMillis();
        this.owned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getLevelRequired() { return levelRequired; }
    public void setLevelRequired(int levelRequired) { this.levelRequired = levelRequired; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getHull() { return hull; }
    public void setHull(int hull) { this.hull = hull; }
    public int getMaxHull() { return maxHull; }
    public void setMaxHull(int maxHull) { this.maxHull = maxHull; }
    public int getShields() { return shields; }
    public void setShields(int shields) { this.shields = shields; }
    public int getMaxShields() { return maxShields; }
    public void setMaxShields(int maxShields) { this.maxShields = maxShields; }
    public int getWeaponSlots() { return weaponSlots; }
    public void setWeaponSlots(int weaponSlots) { this.weaponSlots = weaponSlots; }
    public int getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(int cargoCapacity) { this.cargoCapacity = cargoCapacity; }
    public int getCrewCapacity() { return crewCapacity; }
    public void setCrewCapacity(int crewCapacity) { this.crewCapacity = crewCapacity; }
    public int getJumpRange() { return jumpRange; }
    public void setJumpRange(int jumpRange) { this.jumpRange = jumpRange; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLore() { return lore; }
    public void setLore(String lore) { this.lore = lore; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public boolean isOwned() { return owned; }
    public void setOwned(boolean owned) { this.owned = owned; }
}
