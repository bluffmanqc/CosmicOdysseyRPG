package com.cosmicodyssey.rpg.models;

public enum Rarity {
    COMMON(0xFF9E9E9E, "Commun", 1.0f),
    UNCOMMON(0xFF4CAF50, "Inhabituel", 1.5f),
    RARE(0xFF2196F3, "Rare", 2.0f),
    EPIC(0xFF9C27B0, "Épique", 3.0f),
    LEGENDARY(0xFFFF9800, "Légendaire", 5.0f),
    MYTHIC(0xFFFF1744, "Mythique", 10.0f),
    COSMIC(0xFF00E5FF, "Cosmique", 20.0f);

    private final int color;
    private final String label;
    private final float multiplier;

    Rarity(int color, String label, float multiplier) {
        this.color = color;
        this.label = label;
        this.multiplier = multiplier;
    }

    public int getColor() { return color; }
    public String getLabel() { return label; }
    public float getMultiplier() { return multiplier; }
}
