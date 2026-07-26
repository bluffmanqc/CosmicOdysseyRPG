package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.cosmicodyssey.rpg.models.Character;

public class ProgressionManager {
    
    private static ProgressionManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    
    private int currentLevel;
    private long currentXp;
    private String playerRace;
    
    private ProgressionManager(Context context) {
        prefs = context.getSharedPreferences(ProgressionConfig.PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        
        currentLevel = prefs.getInt(ProgressionConfig.KEY_CURRENT_LEVEL, 1);
        currentXp = prefs.getLong(ProgressionConfig.KEY_CURRENT_XP, 0);
        playerRace = prefs.getString(ProgressionConfig.KEY_RACE, "HUMAIN");
    }
    
    public static synchronized ProgressionManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProgressionManager(context);
        }
        return instance;
    }
    
    public int getCurrentLevel() { return currentLevel; }
    public long getCurrentXp() { return currentXp; }
    public String getPlayerRace() { return playerRace; }
    public String getCurrentZone() { return ProgressionConfig.getZoneName(currentLevel); }
    
    public long getXpToNextLevel() {
        return ProgressionConfig.getXpRequired(currentLevel + 1);
    }
    
    public long getXpForCurrentLevel() {
        return ProgressionConfig.getXpRequired(currentLevel);
    }
    
    public double getProgressPercent() {
        long currentLevelXp = getXpForCurrentLevel();
        long nextLevelXp = getXpToNextLevel();
        long xpInCurrentLevel = currentXp - currentLevelXp;
        long xpNeeded = nextLevelXp - currentLevelXp;
        if (xpNeeded <= 0) return 100.0;
        return (double) xpInCurrentLevel / xpNeeded * 100.0;
    }
    
    public void syncWithCharacter(Character character) {
        if (character != null) {
            playerRace = character.getRace() != null ? character.getRace().toUpperCase() : "HUMAIN";
            currentLevel = character.getLevel();
            currentXp = character.getExperience();
            saveProgress();
        }
    }
    
    public void addXp(long xp) {
        currentXp += xp;
        while (currentXp >= getXpToNextLevel()) {
            levelUp();
        }
        saveProgress();
    }
    
    private void levelUp() {
        currentLevel++;
    }
    
    public void saveProgress() {
        editor.putInt(ProgressionConfig.KEY_CURRENT_LEVEL, currentLevel);
        editor.putLong(ProgressionConfig.KEY_CURRENT_XP, currentXp);
        editor.putString(ProgressionConfig.KEY_CURRENT_ZONE, getCurrentZone());
        editor.putString(ProgressionConfig.KEY_RACE, playerRace);
        editor.apply();
    }
    
    public String getSystemPromptForAI() {
        return ProgressionConfig.getSystemPrompt(playerRace, currentLevel);
    }
    
    public void resetProgress() {
        currentLevel = 1;
        currentXp = 0;
        editor.clear();
        editor.apply();
    }
}
