package com.cosmicodyssey.rpg.models;

import java.util.ArrayList;
import java.util.List;

public class CharacterClass {
    private String name;
    private String description;
    private List<Skill> skillTree;
    
    public CharacterClass(String name, String description) {
        this.name = name;
        this.description = description;
        this.skillTree = new ArrayList<>();
    }
    
    public void addSkill(Skill skill) {
        skillTree.add(skill);
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Skill> getSkillTree() { return skillTree; }
    
    // === CORPS A CORPS ===
    public static CharacterClass getMelee() {
        CharacterClass cls = new CharacterClass("Combattant", "Maitre du combat rapproche spatial");
        cls.addSkill(new Skill("Frappe plasma", "Niveau 1", "Degats +20% au corps a corps"));
        cls.addSkill(new Skill("Parade energetique", "Niveau 2", "Bloque 30% des degats"));
        cls.addSkill(new Skill("Fureur berserker", "Niveau 3", "Force +10 quand PV < 50%"));
        cls.addSkill(new Skill("Lame stellaire", "Niveau 4", "Attaque qui traverse les armures"));
        cls.addSkill(new Skill("Avatar de guerre", "Niveau 5", "Invincibilite 3 tours, cooldown 10 tours"));
        return cls;
    }
    
    // === TIREUR D'ELITE ===
    public static CharacterClass getSniper() {
        CharacterClass cls = new CharacterClass("Tireur d'elite", "Sniper des confins de l'univers");
        cls.addSkill(new Skill("Tir de precision", "Niveau 1", "Degats x2 sur cible isolee"));
        cls.addSkill(new Skill("Camouflage optique", "Niveau 2", "Invisibilite 2 tours, +25% degats apres"));
        cls.addSkill(new Skill("Tir perforant", "Niveau 3", "Ignore 50% de l'armure ennemie"));
        cls.addSkill(new Skill("Drone eclaireur", "Niveau 4", "Drone qui marque les ennemis, +30% precision"));
        cls.addSkill(new Skill("Tir orbital", "Niveau 5", "Frappe depuis l'orbite, degats massifs zone"));
        return cls;
    }
    
    // === HEALER ===
    public static CharacterClass getHealer() {
        CharacterClass cls = new CharacterClass("Soigneur", "Guerisseur des confins de l'espace");
        cls.addSkill(new Skill("Nano-reparation", "Niveau 1", "Restaure 15 PV par tour"));
        cls.addSkill(new Skill("Bouclier biologique", "Niveau 2", "Absorbe 20 degats pour un allie"));
        cls.addSkill(new Skill("Regeneration cellulaire", "Niveau 3", "Guerison passive +5 PV/tour"));
        cls.addSkill(new Skill("Transfusion quantique", "Niveau 4", "Transfere ses PV a un allie"));
        cls.addSkill(new Skill("Resurrection stellaire", "Niveau 5", "Ressuscite un allie une fois par combat"));
        return cls;
    }
    
    // === SORCIER ===
    public static CharacterClass getSorcerer() {
        CharacterClass cls = new CharacterClass("Sorcerer", "Manipulateur des forces cosmiques");
        cls.addSkill(new Skill("Eclair cosmique", "Niveau 1", "Degats energetiques a distance"));
        cls.addSkill(new Skill("Nova stellaire", "Niveau 2", "Explosion area-of-effect"));
        cls.addSkill(new Skill("Distorsion temporelle", "Niveau 3", "Ralentit les ennemis 2 tours"));
        cls.addSkill(new Skill("Trou noir", "Niveau 4", "Aspire et immobilise les ennemis"));
        cls.addSkill(new Skill("Supernova", "Niveau 5", "Degats massifs, cooldown 8 tours"));
        return cls;
    }
    
    // === NECROMANCIEN DE L'ESPACE ===
    public static CharacterClass getNecromancer() {
        CharacterClass cls = new CharacterClass("Necromancien Stellaire", "Maitre des ames perdues dans le vide cosmique");
        cls.addSkill(new Skill("Invocation de spectre", "Niveau 1", "Invoque un fantome spatial qui attaque"));
        cls.addSkill(new Skill("Drain vital cosmique", "Niveau 2", "Vole les PV d'un ennemi"));
        cls.addSkill(new Skill("Armee du vide", "Niveau 3", "Invoque 2 creatures alien mortes"));
        cls.addSkill(new Skill("Malediction stellaire", "Niveau 4", "Reduit toutes les stats d'un ennemi de 50%"));
        cls.addSkill(new Skill("Avatar du neant", "Niveau 5", "Se transforme en etre de pure energie sombre, degats x3"));
        return cls;
    }
    
    public static CharacterClass[] getAllClasses() {
        return new CharacterClass[] {
            getMelee(),
            getSniper(),
            getHealer(),
            getSorcerer(),
            getNecromancer()
        };
    }
    
    public static class Skill {
        private String name;
        private String level;
        private String effect;
        private boolean unlocked;
        private int requiredLevel;
        
        public Skill(String name, String level, String effect) {
            this.name = name;
            this.level = level;
            this.effect = effect;
            this.unlocked = false;
            this.requiredLevel = extractLevel(level);
        }
        
        private int extractLevel(String levelStr) {
            try {
                return Integer.parseInt(levelStr.replaceAll("[^0-9]", ""));
            } catch (Exception e) {
                return 1;
            }
        }
        
        public String getName() { return name; }
        public String getLevel() { return level; }
        public String getEffect() { return effect; }
        public boolean isUnlocked() { return unlocked; }
        public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
        public int getRequiredLevel() { return requiredLevel; }
    }
}
