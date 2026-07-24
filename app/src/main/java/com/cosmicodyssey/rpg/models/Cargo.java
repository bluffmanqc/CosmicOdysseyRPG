package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.UUID;

@Entity
public class Cargo {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String type;
    private String description;
    @Ignore
    private Rarity rarity;
    private int levelRequired;
    private int capacity;
    private int speedPenalty;
    private int shieldBonus;
    private String imageUrl;
    private String lore;
    private String origin;
    private String creatorId;
    private long createdAt;
    private boolean owned;

    public Cargo() {
        this.id = UUID.randomUUID().toString();
        this.rarity = Rarity.COMMON;
        this.createdAt = System.currentTimeMillis();
        this.owned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getLevelRequired() { return levelRequired; }
    public void setLevelRequired(int levelRequired) { this.levelRequired = levelRequired; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getSpeedPenalty() { return speedPenalty; }
    public void setSpeedPenalty(int speedPenalty) { this.speedPenalty = speedPenalty; }
    public int getShieldBonus() { return shieldBonus; }
    public void setShieldBonus(int shieldBonus) { this.shieldBonus = shieldBonus; }
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
