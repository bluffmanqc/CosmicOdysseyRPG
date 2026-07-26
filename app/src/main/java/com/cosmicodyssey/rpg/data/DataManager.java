package com.cosmicodyssey.rpg.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.MarketListing;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Party;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String PREFS_NAME = "CosmicOdysseyData";
    private static final String KEY_CURRENT_CHARACTER = "current_character";
    private static final String KEY_CHARACTERS_LIST = "characters_list";
    private static final String KEY_PARTIES = "parties";
    private static final String KEY_EQUIPMENT_CATALOG = "equipment_catalog";
    private static final String KEY_MOUNT_CATALOG = "mount_catalog";
    private static final String KEY_SPACESHIP_CATALOG = "spaceship_catalog";
    private static final String KEY_CARGO_CATALOG = "cargo_catalog";
    private static final String KEY_MARKET_LISTINGS = "market_listings";
    private static final String KEY_RULES = "rules";

    private final Context context;
    private final SharedPreferences prefs;
    private final Gson gson;
    private final File saveDir;

    public DataManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.saveDir = new File(context.getFilesDir(), "saves");
        if (!saveDir.exists()) saveDir.mkdirs();
    }

    // ==================== PERSONNAGES MULTIPLES ====================

    public void saveCharacter(Character character) {
        String json = gson.toJson(character);
        prefs.edit().putString(KEY_CURRENT_CHARACTER, json).apply();
        saveToFile("character_" + character.getId() + ".json", json);
        addToCharactersList(character);
    }

    private void addToCharactersList(Character character) {
        List<Character> characters = loadAllCharacters();
        characters.removeIf(c -> c.getId().equals(character.getId()));
        characters.add(character);
        prefs.edit().putString(KEY_CHARACTERS_LIST, gson.toJson(characters)).apply();
    }

    public List<Character> loadAllCharacters() {
        String json = prefs.getString(KEY_CHARACTERS_LIST, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<Character>>(){}.getType());
        }
        return new ArrayList<>();
    }

    public void deleteCharacter(String characterId) {
        List<Character> characters = loadAllCharacters();
        characters.removeIf(c -> c.getId().equals(characterId));
        prefs.edit().putString(KEY_CHARACTERS_LIST, gson.toJson(characters)).apply();
        File file = new File(saveDir, "character_" + characterId + ".json");
        if (file.exists()) file.delete();
    }

    public void setCurrentCharacter(String characterId) {
        for (Character c : loadAllCharacters()) {
            if (c.getId().equals(characterId)) {
                prefs.edit().putString(KEY_CURRENT_CHARACTER, gson.toJson(c)).apply();
                return;
            }
        }
    }

    public Character loadCharacter() {
        String json = prefs.getString(KEY_CURRENT_CHARACTER, null);
        if (json != null) {
            return gson.fromJson(json, Character.class);
        }
        List<Character> characters = loadAllCharacters();
        if (!characters.isEmpty()) {
            return characters.get(0);
        }
        return null;
    }

    // ==================== PARTIES ====================

    public void saveParty(Party party) {
        List<Party> parties = loadParties();
        parties.removeIf(p -> p.getId().equals(party.getId()));
        parties.add(party);
        String json = gson.toJson(parties);
        prefs.edit().putString(KEY_PARTIES, json).apply();
        saveToFile("party_" + party.getId() + ".json", gson.toJson(party));
    }

    public List<Party> loadParties() {
        String json = prefs.getString(KEY_PARTIES, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<Party>>(){}.getType());
        }
        return new ArrayList<>();
    }

    public Party loadParty(String partyId) {
        File file = new File(saveDir, "party_" + partyId + ".json");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                return gson.fromJson(reader, Party.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Party p : loadParties()) {
            if (p.getId().equals(partyId)) return p;
        }
        return null;
    }

    // ==================== CATALOGUES ====================

    public void addToCatalog(Equipment equipment) {
        List<Equipment> catalog = loadEquipmentCatalog();
        catalog.removeIf(e -> e.getId().equals(equipment.getId()));
        catalog.add(equipment);
        prefs.edit().putString(KEY_EQUIPMENT_CATALOG, gson.toJson(catalog)).apply();
    }

    public List<Equipment> loadEquipmentCatalog() {
        String json = prefs.getString(KEY_EQUIPMENT_CATALOG, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<Equipment>>(){}.getType());
        }
        return new ArrayList<>();
    }

    public void addToCatalog(Mount mount) {
        List<Mount> catalog = loadMountCatalog();
        catalog.removeIf(m -> m.getId().equals(mount.getId()));
        catalog.add(mount);
        prefs.edit().putString(KEY_MOUNT_CATALOG, gson.toJson(catalog)).apply();
    }

    public List<Mount> loadMountCatalog() {
        String json = prefs.getString(KEY_MOUNT_CATALOG, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<Mount>>(){}.getType());
        }
        return new ArrayList<>();
    }

    public void addToCatalog(Spaceship spaceship) {
        List<Spaceship> catalog = loadSpaceshipCatalog();
        catalog.removeIf(s -> s.getId().equals(spaceship.getId()));
        catalog.add(spaceship);
        prefs.edit().putString(KEY_SPACESHIP_CATALOG, gson.toJson(catalog)).apply();
    }

    public List<Spaceship> loadSpaceshipCatalog() {
        String json = prefs.getString(KEY_SPACESHIP_CATALOG, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<Spaceship>>(){}.getType());
        }
        return new ArrayList<>();
    }

    public void mergeCatalogsFromParty(Party party) {
        for (Equipment e : party.getSharedEquipment()) addToCatalog(e);
        for (Mount m : party.getSharedMounts()) addToCatalog(m);
        for (Spaceship s : party.getSharedSpaceships()) addToCatalog(s);
    }

    // ==================== MARCHE NOIR ====================

    public void addMarketListing(MarketListing listing) {
        List<MarketListing> listings = loadMarketListings();
        listings.add(listing);
        prefs.edit().putString(KEY_MARKET_LISTINGS, gson.toJson(listings)).apply();
    }

    public void removeMarketListing(MarketListing listing) {
        List<MarketListing> listings = loadMarketListings();
        listings.removeIf(l -> l.getId().equals(listing.getId()));
        prefs.edit().putString(KEY_MARKET_LISTINGS, gson.toJson(listings)).apply();
    }

    public List<MarketListing> loadMarketListings() {
        String json = prefs.getString(KEY_MARKET_LISTINGS, null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<List<MarketListing>>(){}.getType());
        }
        return new ArrayList<>();
    }

    private void saveToFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(new File(saveDir, filename));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSaveDirectory() {
        return saveDir.getAbsolutePath();
    }
}
