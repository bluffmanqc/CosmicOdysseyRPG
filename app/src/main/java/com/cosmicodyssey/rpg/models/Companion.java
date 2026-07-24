package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Companion {
    public enum Type { BEAST, MERCENARY, ROBOT, ALIEN, SPIRIT }
    public enum Role { ATTACKER, DEFENDER, HEALER, BUFFER, SCOUT }

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private Type type;
    private Role role;
    private String description;
    private String imageUrl;
    private int level;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private String buffType;
    private int buffValue;
    private boolean isActive;
    private String planetOrigin;
    private int captureCost;
    private int buyCost;

    public Companion() {
        this.id = java.util.UUID.randomUUID().toString();
        this.level = 1;
        this.isActive = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public String getBuffType() { return buffType; }
    public void setBuffType(String buffType) { this.buffType = buffType; }
    public int getBuffValue() { return buffValue; }
    public void setBuffValue(int buffValue) { this.buffValue = buffValue; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getPlanetOrigin() { return planetOrigin; }
    public void setPlanetOrigin(String planetOrigin) { this.planetOrigin = planetOrigin; }
    public int getCaptureCost() { return captureCost; }
    public void setCaptureCost(int captureCost) { this.captureCost = captureCost; }
    public int getBuyCost() { return buyCost; }
    public void setBuyCost(int buyCost) { this.buyCost = buyCost; }
}