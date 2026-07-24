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
    
    // === CORPS À CORPS ===
    public static CharacterClass getMelee() {
        CharacterClass cls = new CharacterClass("Combattant", "Maître du combat rapproché spatial");
        cls.addSkill(new Skill("Frappe plasma", "Niveau 1", "Dégâts +20% au corps à corps"));
        cls.addSkill(new Skill("Parade énergétique", "Niveau 2", "Bloque 30% des dégâts"));
        cls.addSkill(new Skill("Fureur berserker", "Niveau 3", "Force +10 quand PV < 50%"));
        cls.addSkill(new Skill("Lame stellaire", "Niveau 4", "Attaque qui traverse les armures"));
        cls.addSkill(new Skill("Avatar de guerre", "Niveau 5", "Invincibilité 3 tours, cooldown 10 tours"));
        return cls;
    }
    
    // === HEALER ===
    public static CharacterClass getHealer() {
        CharacterClass cls = new CharacterClass("Soigneur", "Guérisseur des confins de l'espace");
        cls.addSkill(new Skill("Nano-réparation", "Niveau 1", "Restaure 15 PV par tour"));
        cls.addSkill(new Skill("Bouclier biologique", "Niveau 2", "Absorbe 20 dégâts pour un allié"));
        cls.addSkill(new Skill("Régénération cellulaire", "Niveau 3", "Guérison passive +5 PV/tour"));
        cls.addSkill(new Skill("Transfusion quantique", "Niveau 4", "Transfère ses PV à un allié"));
        cls.addSkill(new Skill("Résurrection stellaire", "Niveau 5", "Ressuscite un allié une fois par combat"));
        return cls;
    }
    
    // === SORCIER ===
    public static CharacterClass getSorcerer() {
        CharacterClass cls = new CharacterClass("Sorcerer", "Manipulateur des forces cosmiques");
        cls.addSkill(new Skill("Éclair cosmique", "Niveau 1", "Dégâts énergétiques à distance"));
        cls.addSkill(new Skill("Nova stellaire", "Niveau 2", "Explosion area-of-effect"));
        cls.addSkill(new Skill("Distorsion temporelle", "Niveau 3", "Ralentit les ennemis 2 tours"));
        cls.addSkill(new Skill("Trou noir", "Niveau 4", "Aspire et immobilise les ennemis"));
        cls.addSkill(new Skill("Supernova", "Niveau 5", "Dégâts massifs, cooldown 8 tours"));
        return cls;
    }
    
    // === NÉCROMANCIEN DE L'ESPACE ===
    public static CharacterClass getNecromancer() {
        CharacterClass cls = new CharacterClass("Nécromancien Stellaire", "Maître des âmes perdues dans le vide cosmique");
        cls.addSkill(new Skill("Invocation de spectre", "Niveau 1", "Invoque un fantôme spatial qui attaque"));
        cls.addSkill(new Skill("Drain vital cosmique", "Niveau 2", "Vole les PV d'un ennemi"));
        cls.addSkill(new Skill("Armée du vide", "Niveau 3", "Invoque 2 créatures alien mortes"));
        cls.addSkill(new Skill("Malédiction stellaire", "Niveau 4", "Réduit toutes les stats d'un ennemi de 50%"));
        cls.addSkill(new Skill("Avatar du néant", "Niveau 5", "Se transforme en être de pure énergie sombre, dégâts x3"));
        return cls;
    }
    
    public static CharacterClass[] getAllClasses() {
        return new CharacterClass[] {
            getMelee(),
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
