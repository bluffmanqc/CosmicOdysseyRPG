package com.cosmicodyssey.rpg.models;

public class Vehicle {
    private String id;
    private String name;
    private String type; // MOUNT, SPACESHIP, CARGO
    private int cargoCapacity;
    private int speed;
    private String armament;
    private boolean owned;

    public Vehicle() {}

    public Vehicle(String id, String name, String type, int cargoCapacity, int speed, String armament) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cargoCapacity = cargoCapacity;
        this.speed = speed;
        this.armament = armament;
        this.owned = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(int cargoCapacity) { this.cargoCapacity = cargoCapacity; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public String getArmament() { return armament; }
    public void setArmament(String armament) { this.armament = armament; }
    public boolean isOwned() { return owned; }
    public void setOwned(boolean owned) { this.owned = owned; }

    public String getTravelRange() {
        switch (type) {
            case "MOUNT": return "INTER_CITY";
            case "SPACESHIP": return "INTER_PLANET";
            case "CARGO": return "INTER_SYSTEM";
            default: return "NONE";
        }
    }
}
