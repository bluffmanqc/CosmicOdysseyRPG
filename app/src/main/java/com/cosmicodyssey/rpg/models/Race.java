package com.cosmicodyssey.rpg.models;

public class Race {
    private String name;
    private int baseSTR, baseDEX, baseCON, baseINT, baseSAG, baseCHA, baseLUCK;
    private int maxSTR, maxDEX, maxCON, maxINT, maxSAG, maxCHA, maxLUCK;
    private String specialAbility;
    
    public Race(String name, int baseSTR, int baseDEX, int baseCON, int baseINT, 
                int baseSAG, int baseCHA, int baseLUCK,
                int maxSTR, int maxDEX, int maxCON, int maxINT,
                int maxSAG, int maxCHA, int maxLUCK, String specialAbility) {
        this.name = name;
        this.baseSTR = baseSTR;
        this.baseDEX = baseDEX;
        this.baseCON = baseCON;
        this.baseINT = baseINT;
        this.baseSAG = baseSAG;
        this.baseCHA = baseCHA;
        this.baseLUCK = baseLUCK;
        this.maxSTR = maxSTR;
        this.maxDEX = maxDEX;
        this.maxCON = maxCON;
        this.maxINT = maxINT;
        this.maxSAG = maxSAG;
        this.maxCHA = maxCHA;
        this.maxLUCK = maxLUCK;
        this.specialAbility = specialAbility;
    }
    
    public String getName() { return name; }
    public int getBaseSTR() { return baseSTR; }
    public int getBaseDEX() { return baseDEX; }
    public int getBaseCON() { return baseCON; }
    public int getBaseINT() { return baseINT; }
    public int getBaseSAG() { return baseSAG; }
    public int getBaseCHA() { return baseCHA; }
    public int getBaseLUCK() { return baseLUCK; }
    public int getMaxSTR() { return maxSTR; }
    public int getMaxDEX() { return maxDEX; }
    public int getMaxCON() { return maxCON; }
    public int getMaxINT() { return maxINT; }
    public int getMaxSAG() { return maxSAG; }
    public int getMaxCHA() { return maxCHA; }
    public int getMaxLUCK() { return maxLUCK; }
    public String getSpecialAbility() { return specialAbility; }
    
    public static Race getTerrien() {
        return new Race("Terrien", 10, 10, 10, 10, 10, 10, 10,
                       20, 20, 20, 20, 20, 20, 20, "Polyvalent - Bonus +5 dans toutes les stats");
    }
    
    public static Race getLogicus() {
        return new Race("Logicus", 8, 10, 10, 16, 14, 8, 10,
                       16, 18, 18, 24, 22, 16, 18, "Intelligence supérieure, émotions contrôlées");
    }
    
    public static Race getKthari() {
        return new Race("K'thari", 14, 10, 14, 8, 10, 8, 10,
                       24, 18, 24, 16, 18, 16, 18, "Guerriers honorables, force et constitution accrues");
    }
    
    public static Race getNebuleux() {
        return new Race("Nébuleux", 6, 12, 8, 12, 12, 16, 12,
                       14, 20, 16, 20, 20, 24, 20, "Êtres énergétiques, charisme et chance élevés");
    }
    
    public static Race getMechanis() {
        return new Race("Mécanis", 12, 10, 16, 12, 8, 6, 10,
                       22, 18, 26, 20, 16, 14, 18, "Cyborgs naturels, constitution et force augmentées");
    }
    
    public static Race getAstralien() {
        return new Race("Astralien", 8, 10, 8, 14, 16, 12, 14,
                       16, 18, 16, 22, 24, 20, 22, "Télépathes, sagesse et intelligence supérieures");
    }

    public static Race[] getAllRaces() {
        return new Race[] {
            getTerrien(),
            getLogicus(),
            getKthari(),
            getNebuleux(),
            getMechanis(),
            getAstralien()
        };
    }
}

// À ajouter à la fin de la classe Race, avant la dernière accolade
