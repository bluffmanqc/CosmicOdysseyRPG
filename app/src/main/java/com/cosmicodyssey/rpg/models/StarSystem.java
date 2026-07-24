package com.cosmicodyssey.rpg.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

@Entity
public class StarSystem {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String starType;
    private String description;
    private String imageUrl;

    @Ignore
    private List<Planet> planets;
    private double x;
    private double y;
    private boolean discovered;
    private boolean connected;
    private String faction;
    private int threatLevel;

    public StarSystem() {
        this.id = UUID.randomUUID().toString();
        this.planets = new ArrayList<>();
        this.discovered = false;
        this.connected = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStarType() { return starType; }
    public void setStarType(String starType) { this.starType = starType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<Planet> getPlanets() { return planets; }
    public void setPlanets(List<Planet> planets) { this.planets = planets; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public boolean isDiscovered() { return discovered; }
    public void setDiscovered(boolean discovered) { this.discovered = discovered; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public String getFaction() { return faction; }
    public void setFaction(String faction) { this.faction = faction; }
    public int getThreatLevel() { return threatLevel; }
    public void setThreatLevel(int threatLevel) { this.threatLevel = threatLevel; }
}
