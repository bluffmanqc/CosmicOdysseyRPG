package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.UUID;

@Entity
public class Mount {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String species;
    private String description;
    @Ignore
    private Rarity rarity;
    private int levelRequired;
    private int speed;
    private int cargoCapacity;
    private int combatBonus;
    private String imageUrl;
    private String lore;
    private String origin;
    private String creatorId;
    private long createdAt;
    private boolean owned;

    public Mount() {
        this.id = UUID.randomUUID().toString();
        this.rarity = Rarity.COMMON;
        this.createdAt = System.currentTimeMillis();
        this.owned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getLevelRequired() { return levelRequired; }
    public void setLevelRequired(int levelRequired) { this.levelRequired = levelRequired; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(int cargoCapacity) { this.cargoCapacity = cargoCapacity; }
    public int getCombatBonus() { return combatBonus; }
    public void setCombatBonus(int combatBonus) { this.combatBonus = combatBonus; }
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
