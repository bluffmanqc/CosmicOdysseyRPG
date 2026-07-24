package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Planet {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String systemId;
    private String biome;
    private String description;
    private int dangerLevel;
    private int resourceLevel;
    private String imageUrl;
    private String mapImageUrl;

    @Ignore
    private List<String> pointsOfInterest;

    @Ignore
    private List<String> availableMissions;

    @Ignore
    private List<ShopItem> shopItems;
    private boolean discovered;
    private boolean visited;
    private double x;
    private double y;
    private String atmosphere;
    private String gravity;
    private String temperature;
    private String dominantFaction;

    public Planet() {
        this.id = UUID.randomUUID().toString();
        this.pointsOfInterest = new ArrayList<>();
        this.availableMissions = new ArrayList<>();
        this.shopItems = new ArrayList<>();
        this.discovered = false;
        this.visited = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getBiome() { return biome; }
    public void setBiome(String biome) { this.biome = biome; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDangerLevel() { return dangerLevel; }
    public void setDangerLevel(int dangerLevel) { this.dangerLevel = dangerLevel; }
    public int getResourceLevel() { return resourceLevel; }
    public void setResourceLevel(int resourceLevel) { this.resourceLevel = resourceLevel; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getMapImageUrl() { return mapImageUrl; }
    public void setMapImageUrl(String mapImageUrl) { this.mapImageUrl = mapImageUrl; }
    public List<String> getPointsOfInterest() { return pointsOfInterest; }
    public void setPointsOfInterest(List<String> pointsOfInterest) { this.pointsOfInterest = pointsOfInterest; }
    public List<String> getAvailableMissions() { return availableMissions; }
    public void setAvailableMissions(List<String> availableMissions) { this.availableMissions = availableMissions; }
    public List<ShopItem> getShopItems() { return shopItems; }
    public void setShopItems(List<ShopItem> shopItems) { this.shopItems = shopItems; }
    public boolean isDiscovered() { return discovered; }
    public void setDiscovered(boolean discovered) { this.discovered = discovered; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public String getAtmosphere() { return atmosphere; }
    public void setAtmosphere(String atmosphere) { this.atmosphere = atmosphere; }
    public String getGravity() { return gravity; }
    public void setGravity(String gravity) { this.gravity = gravity; }
    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }
    public String getDominantFaction() { return dominantFaction; }
    public void setDominantFaction(String dominantFaction) { this.dominantFaction = dominantFaction; }
}
