package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


@Entity
public class Dice {
    public enum DiceType {
        D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);
        
        private final int sides;
        DiceType(int sides) { this.sides = sides; }
        public int getSides() { return sides; }
    }

    @Ignore
    private DiceType type;
    @PrimaryKey

    @NonNull

    private int  result;;
    private int modifier;
    private boolean criticalSuccess;
    private boolean criticalFail;
    private boolean isRolling;
    private String rollAnimation;

    public Dice(DiceType type) {
        this.type = type;
        this.modifier = 0;
    }

    public int roll() {
        isRolling = true;
        result = (int)(Math.random() * type.getSides()) + 1;
        criticalSuccess = (result == type.getSides());
        criticalFail = (result == 1);
        isRolling = false;
        return result + modifier;
    }

    public DiceType getType() { return type; }
    public void setType(DiceType type) { this.type = type; }
    public int getResult() { return result; }
    public void setResult(int result) { this.result = result; }
    public int getModifier() { return modifier; }
    public void setModifier(int modifier) { this.modifier = modifier; }
    public boolean isCriticalSuccess() { return criticalSuccess; }
    public boolean isCriticalFail() { return criticalFail; }
    public boolean isRolling() { return isRolling; }
    public String getRollAnimation() { return rollAnimation; }
    public void setRollAnimation(String rollAnimation) { this.rollAnimation = rollAnimation; }
}
