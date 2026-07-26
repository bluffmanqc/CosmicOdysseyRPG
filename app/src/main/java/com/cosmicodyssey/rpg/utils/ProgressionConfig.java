package com.cosmicodyssey.rpg.utils;

public class ProgressionConfig {
    
    private static final long BASE_XP = 100;
    
    public static long getXpRequired(int level) {
        if (level <= 1) return 0;
        
        double exponent;
        if (level <= 25) exponent = 1.8;
        else if (level <= 50) exponent = 2.2;
        else if (level <= 75) exponent = 2.8;
        else if (level <= 90) exponent = 3.5;
        else if (level <= 95) exponent = 4.5;
        else if (level <= 99) exponent = 5.5;
        else exponent = 6.0;
        
        long xp = (long) (BASE_XP * Math.pow(level, exponent));
        
        if (xp > 1000000) {
            xp = (xp / 100000) * 100000;
        } else if (xp > 10000) {
            xp = (xp / 1000) * 1000;
        }
        
        return xp;
    }
    
    public static String getZoneName(int level) {
        if (level <= 5) return "Planète Natale";
        else if (level <= 10) return "Région Locale";
        else if (level <= 15) return "Système Planétaire";
        else if (level <= 25) return "Système Solaire";
        else if (level <= 40) return "Secteur Galactique";
        else if (level <= 60) return "Galaxie Voisine";
        else if (level <= 80) return "Amas de Galaxies";
        else if (level <= 100) return "Univers Parallèle";
        else return "Dimension " + (level - 100);
    }
    
    public static String getRequiredVehicle(int level) {
        if (level <= 5) return "Aucun (à pied)";
        else if (level <= 10) return "Monture";
        else if (level <= 15) return "Vaisseau Léger";
        else if (level <= 25) return "Cargo";
        else if (level <= 40) return "Vaisseau de Guerre";
        else if (level <= 60) return "Portail Intergalactique";
        else if (level <= 80) return "Technologie Ancêtre";
        else if (level <= 100) return "Réalité Altérée";
        else return "Conscience Cosmique";
    }
    
    public static String getDropRarityTier(int level) {
        if (level <= 10) return "COMMUN";
        else if (level <= 20) return "RARE";
        else if (level <= 35) return "ÉPIQUE";
        else if (level <= 50) return "LÉGENDAIRE";
        else if (level <= 70) return "MYTHIQUE";
        else if (level <= 85) return "COSMIQUE";
        else if (level <= 95) return "DIMENSIONNEL";
        else if (level <= 100) return "TRANSCENDANT";
        else return "INCONNU_" + (level - 100);
    }
    
    public static double getDropChance(int level, String rarity) {
        double baseChance;
        switch (rarity) {
            case "COMMUN": baseChance = 0.30; break;
            case "RARE": baseChance = 0.15; break;
            case "ÉPIQUE": baseChance = 0.05; break;
            case "LÉGENDAIRE": baseChance = 0.01; break;
            case "MYTHIQUE": baseChance = 0.003; break;
            case "COSMIQUE": baseChance = 0.001; break;
            case "DIMENSIONNEL": baseChance = 0.0003; break;
            case "TRANSCENDANT": baseChance = 0.0001; break;
            default: baseChance = 0.00001; break;
        }
        
        double levelPenalty = 1.0 - ((level - 1) * 0.005);
        if (levelPenalty < 0.1) levelPenalty = 0.1;
        
        return baseChance * levelPenalty;
    }
    
    public static String getRaceContext(String race) {
        switch (race.toUpperCase()) {
            case "HUMAIN":
                return "Tu es un Humain originaire d'une planète industrielle type Terre. Tu as commencé dans une ville surpeuplée et tu dois t'élever. Les humains sont polyvalents, adaptables et ambitieux.";
            case "ELF":
                return "Tu es un Elf originaire d'une planète forestière ancienne. Tu maîtrises la magie naturelle et la technologie organique. Les elfes sont sages, agiles et connectés à la nature.";
            case "NAIN":
                return "Tu es un Nain originaire d'une planète minière souterraine. Tu excelles en ingénierie et combat rapproché. Les nains sont résistants, ingénieux et tenaces.";
            case "ANDROID":
                return "Tu es un Android créé artificiellement, conscience éveillée. Tu as des capacités de hacking et de calcul supérieures. Les androids sont logiques, rapides et insensibles à la douleur physique.";
            default:
                return "Tu es un " + race + " avec tes propres capacités et histoire unique.";
        }
    }
    
    public static String getSystemPrompt(String race, int level) {
        String zone = getZoneName(level);
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Tu es le Maître du Jeu d'un RPG spatial sans fin appelé Cosmic Odyssey. ");
        prompt.append(getRaceContext(race));
        prompt.append(" Tu es actuellement au niveau ").append(level);
        prompt.append(", dans la zone : ").append(zone).append(". ");
        
        if (level <= 10) {
            prompt.append("Ton narratif : Aventure locale, menaces terrestres, quêtes simples. ");
            prompt.append("Le joueur commence en sous-vêtements/simples habits, sans arme, sans vaisseau, avec quelques crédits. ");
            prompt.append("Objectif : Quitter la planète natale et acquérir un vaisseau. ");
        } else if (level <= 25) {
            prompt.append("Ton narratif : Exploration spatiale, pirates, factions locales. ");
            prompt.append("Le joueur voyage maintenant entre planètes. ");
        } else if (level <= 50) {
            prompt.append("Ton narratif : Guerre galactique, empires, technologies avancées. ");
            prompt.append("Le joueur est un vétéran de l'espace. ");
        } else if (level <= 75) {
            prompt.append("Ton narratif : Cosmique, entités anciennes, réalités alternatives. ");
            prompt.append("Le joueur manipule des forces au-delà de la compréhension normale. ");
        } else if (level <= 100) {
            prompt.append("Ton narratif : Transcendant, manipulation du temps/espace, dieux anciens. ");
            prompt.append("Le joueur est une entité quasi-divine. ");
        } else {
            prompt.append("Ton narratif : AU-DELÀ DE LA COMPREHENSION. ");
            prompt.append("Le joueur est une entité cosmique explorant des dimensions inconnues. ");
            prompt.append("Chaque niveau au-delà de 100 est une nouvelle couche de réalité jamais explorée. ");
        }
        
        prompt.append("RÈGLES CRITIQUES : ");
        prompt.append("1. Génère des équipements avec des noms procéduraux uniques (ex: 'Épée du Vide Stellaire [+").append(level).append("]'). ");
        prompt.append("2. Les stats augmentent avec le niveau mais deviennent de plus en plus difficiles à obtenir. ");
        prompt.append("3. Au niveau ").append(level).append(", un équipement légendaire a un drop de ");
        prompt.append(String.format("%.4f", getDropChance(level, "LÉGENDAIRE") * 100)).append("%. ");
        prompt.append("4. Le joueur doit TOUJOURS avoir un objectif à atteindre, même au niveau 100+. ");
        prompt.append("5. Même au niveau 100+, il y a toujours un équipement meilleur à trouver. ");
        prompt.append("6. La difficulté s'adapte : les ennemis sont proportionnels au niveau du joueur. ");
        prompt.append("7. Le jeu est SANS FIN. Il n'y a pas de fin, seulement des objectifs toujours plus grands. ");
        prompt.append("8. Génère des équipements procéduraux avec des noms uniques basés sur le niveau et la zone. ");
        
        return prompt.toString();
    }
    
    public static final String PREFS_NAME = "GameProgression";
    public static final String KEY_CURRENT_LEVEL = "current_level";
    public static final String KEY_CURRENT_XP = "current_xp";
    public static final String KEY_CURRENT_ZONE = "current_zone";
    public static final String KEY_RACE = "player_race";

    public static final String VEHICLE_MOUNT = "MOUNT";
    public static final String VEHICLE_SPACESHIP = "SPACESHIP";
    public static final String VEHICLE_CARGO = "CARGO";

    public static java.util.List<com.cosmicodyssey.rpg.models.Vehicle> getAvailableVehicles() {
        java.util.List<com.cosmicodyssey.rpg.models.Vehicle> vehicles = new java.util.ArrayList<>();
        vehicles.add(new com.cosmicodyssey.rpg.models.Vehicle("mount_1", "Cheval mecanique", VEHICLE_MOUNT, 50, 40, null));
        vehicles.add(new com.cosmicodyssey.rpg.models.Vehicle("mount_2", "Levitateur", VEHICLE_MOUNT, 30, 60, null));
        vehicles.add(new com.cosmicodyssey.rpg.models.Vehicle("ship_1", "Chasseur leger", VEHICLE_SPACESHIP, 100, 80, "Laser leger"));
        vehicles.add(new com.cosmicodyssey.rpg.models.Vehicle("ship_2", "Cargo spatial", VEHICLE_SPACESHIP, 500, 50, null));
        vehicles.add(new com.cosmicodyssey.rpg.models.Vehicle("cargo_1", "Vaisseau de ligne", VEHICLE_CARGO, 2000, 30, "Canons plasma"));
        return vehicles;
    }
}
