package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


@Entity
public class CharacterStats {
    @PrimaryKey

    @NonNull

    private int  strength;;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int luck;
    private int psionics;
    private int technology;

    public CharacterStats() {
        this.strength = 10;
        this.dexterity = 10;
        this.constitution = 10;
        this.intelligence = 10;
        this.wisdom = 10;
        this.charisma = 10;
        this.luck = 10;
        this.psionics = 0;
        this.technology = 0;
    }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public int getConstitution() { return constitution; }
    public void setConstitution(int constitution) { this.constitution = constitution; }
    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
    public int getWisdom() { return wisdom; }
    public void setWisdom(int wisdom) { this.wisdom = wisdom; }
    public int getCharisma() { return charisma; }
    public void setCharisma(int charisma) { this.charisma = charisma; }
    public int getLuck() { return luck; }
    public void setLuck(int luck) { this.luck = luck; }
    public int getPsionics() { return psionics; }
    public void setPsionics(int psionics) { this.psionics = psionics; }
    public int getTechnology() { return technology; }
    public void setTechnology(int technology) { this.technology = technology; }

    public int getModifier(int stat) {
        return (stat - 10) / 2;
    }
}
