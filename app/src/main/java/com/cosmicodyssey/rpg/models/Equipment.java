package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.UUID;

@Entity
public class Equipment {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String description;
    private String type;
    @Ignore
    private Rarity rarity;
    private int levelRequired;
    private int damage;
    private int defense;
    private int shieldBonus;
    private int energyBonus;
    private int strengthBonus;
    private int dexterityBonus;
    private int intelligenceBonus;
    private int psionicsBonus;
    private int technologyBonus;
    private String imageUrl;
    private String lore;
    private String origin;
    private String creatorId;
    private long createdAt;
    private boolean equipped;

    public Equipment() {
        this.id = UUID.randomUUID().toString();
        this.rarity = Rarity.COMMON;
        this.createdAt = System.currentTimeMillis();
        this.equipped = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getLevelRequired() { return levelRequired; }
    public void setLevelRequired(int levelRequired) { this.levelRequired = levelRequired; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getShieldBonus() { return shieldBonus; }
    public void setShieldBonus(int shieldBonus) { this.shieldBonus = shieldBonus; }
    public int getEnergyBonus() { return energyBonus; }
    public void setEnergyBonus(int energyBonus) { this.energyBonus = energyBonus; }
    public int getStrengthBonus() { return strengthBonus; }
    public void setStrengthBonus(int strengthBonus) { this.strengthBonus = strengthBonus; }
    public int getDexterityBonus() { return dexterityBonus; }
    public void setDexterityBonus(int dexterityBonus) { this.dexterityBonus = dexterityBonus; }
    public int getIntelligenceBonus() { return intelligenceBonus; }
    public void setIntelligenceBonus(int intelligenceBonus) { this.intelligenceBonus = intelligenceBonus; }
    public int getPsionicsBonus() { return psionicsBonus; }
    public void setPsionicsBonus(int psionicsBonus) { this.psionicsBonus = psionicsBonus; }
    public int getTechnologyBonus() { return technologyBonus; }
    public void setTechnologyBonus(int technologyBonus) { this.technologyBonus = technologyBonus; }
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
    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }
}
