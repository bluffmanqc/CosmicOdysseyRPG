package com.cosmicodyssey.rpg.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Character` (`id` TEXT NOT NULL, `name` TEXT, `race` TEXT, `className` TEXT, `background` TEXT, `alignment` TEXT, `level` INTEGER NOT NULL, `experience` INTEGER NOT NULL, `hitPoints` INTEGER NOT NULL, `maxHitPoints` INTEGER NOT NULL, `shieldPoints` INTEGER NOT NULL, `maxShieldPoints` INTEGER NOT NULL, `energy` INTEGER NOT NULL, `maxEnergy` INTEGER NOT NULL, `credits` INTEGER NOT NULL, `avatarUrl` TEXT, `avatarType` TEXT, `description` TEXT, `story` TEXT, `skillPoints` INTEGER NOT NULL, `skills` TEXT, `currentPlanet` TEXT, `currentSystem` TEXT, `partyId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Equipment` (`id` TEXT NOT NULL, `name` TEXT, `description` TEXT, `type` TEXT, `levelRequired` INTEGER NOT NULL, `damage` INTEGER NOT NULL, `defense` INTEGER NOT NULL, `shieldBonus` INTEGER NOT NULL, `energyBonus` INTEGER NOT NULL, `strengthBonus` INTEGER NOT NULL, `dexterityBonus` INTEGER NOT NULL, `intelligenceBonus` INTEGER NOT NULL, `psionicsBonus` INTEGER NOT NULL, `technologyBonus` INTEGER NOT NULL, `imageUrl` TEXT, `lore` TEXT, `origin` TEXT, `creatorId` TEXT, `createdAt` INTEGER NOT NULL, `equipped` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Mount` (`id` TEXT NOT NULL, `name` TEXT, `species` TEXT, `description` TEXT, `levelRequired` INTEGER NOT NULL, `speed` INTEGER NOT NULL, `cargoCapacity` INTEGER NOT NULL, `combatBonus` INTEGER NOT NULL, `imageUrl` TEXT, `lore` TEXT, `origin` TEXT, `creatorId` TEXT, `createdAt` INTEGER NOT NULL, `owned` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Party` (`id` TEXT NOT NULL, `name` TEXT, `description` TEXT, `gameMasterType` TEXT, `difficulty` TEXT, `hostId` TEXT, `currentPlanet` TEXT, `currentSystem` TEXT, `storyPrompt` TEXT, `storyHistory` TEXT, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `lastActivity` INTEGER NOT NULL, `partyCode` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Planet` (`id` TEXT NOT NULL, `name` TEXT, `systemId` TEXT, `biome` TEXT, `description` TEXT, `dangerLevel` INTEGER NOT NULL, `resourceLevel` INTEGER NOT NULL, `imageUrl` TEXT, `mapImageUrl` TEXT, `discovered` INTEGER NOT NULL, `visited` INTEGER NOT NULL, `x` REAL NOT NULL, `y` REAL NOT NULL, `atmosphere` TEXT, `gravity` TEXT, `temperature` TEXT, `dominantFaction` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Rule` (`id` TEXT NOT NULL, `title` TEXT, `category` TEXT, `content` TEXT, `imageUrl` TEXT, `source` TEXT, `authorId` TEXT, `createdAt` INTEGER NOT NULL, `isOfficial` INTEGER NOT NULL, `pageNumber` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Spaceship` (`id` TEXT NOT NULL, `name` TEXT, `model` TEXT, `description` TEXT, `levelRequired` INTEGER NOT NULL, `speed` INTEGER NOT NULL, `hull` INTEGER NOT NULL, `maxHull` INTEGER NOT NULL, `shields` INTEGER NOT NULL, `maxShields` INTEGER NOT NULL, `weaponSlots` INTEGER NOT NULL, `cargoCapacity` INTEGER NOT NULL, `crewCapacity` INTEGER NOT NULL, `jumpRange` INTEGER NOT NULL, `imageUrl` TEXT, `lore` TEXT, `origin` TEXT, `creatorId` TEXT, `createdAt` INTEGER NOT NULL, `owned` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `StarSystem` (`id` TEXT NOT NULL, `name` TEXT, `starType` TEXT, `description` TEXT, `imageUrl` TEXT, `x` REAL NOT NULL, `y` REAL NOT NULL, `discovered` INTEGER NOT NULL, `connected` INTEGER NOT NULL, `faction` TEXT, `threatLevel` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c0b3caad3dbd89f4a089b108b476f74f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Character`");
        db.execSQL("DROP TABLE IF EXISTS `Equipment`");
        db.execSQL("DROP TABLE IF EXISTS `Mount`");
        db.execSQL("DROP TABLE IF EXISTS `Party`");
        db.execSQL("DROP TABLE IF EXISTS `Planet`");
        db.execSQL("DROP TABLE IF EXISTS `Rule`");
        db.execSQL("DROP TABLE IF EXISTS `Spaceship`");
        db.execSQL("DROP TABLE IF EXISTS `StarSystem`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCharacter = new HashMap<String, TableInfo.Column>(26);
        _columnsCharacter.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("race", new TableInfo.Column("race", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("className", new TableInfo.Column("className", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("background", new TableInfo.Column("background", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("alignment", new TableInfo.Column("alignment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("experience", new TableInfo.Column("experience", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("hitPoints", new TableInfo.Column("hitPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("maxHitPoints", new TableInfo.Column("maxHitPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("shieldPoints", new TableInfo.Column("shieldPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("maxShieldPoints", new TableInfo.Column("maxShieldPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("energy", new TableInfo.Column("energy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("maxEnergy", new TableInfo.Column("maxEnergy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("credits", new TableInfo.Column("credits", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("avatarUrl", new TableInfo.Column("avatarUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("avatarType", new TableInfo.Column("avatarType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("story", new TableInfo.Column("story", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("skillPoints", new TableInfo.Column("skillPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("skills", new TableInfo.Column("skills", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("currentPlanet", new TableInfo.Column("currentPlanet", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("currentSystem", new TableInfo.Column("currentSystem", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("partyId", new TableInfo.Column("partyId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacter.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharacter = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharacter = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharacter = new TableInfo("Character", _columnsCharacter, _foreignKeysCharacter, _indicesCharacter);
        final TableInfo _existingCharacter = TableInfo.read(db, "Character");
        if (!_infoCharacter.equals(_existingCharacter)) {
          return new RoomOpenHelper.ValidationResult(false, "Character(com.cosmicodyssey.rpg.models.Character).\n"
                  + " Expected:\n" + _infoCharacter + "\n"
                  + " Found:\n" + _existingCharacter);
        }
        final HashMap<String, TableInfo.Column> _columnsEquipment = new HashMap<String, TableInfo.Column>(20);
        _columnsEquipment.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("levelRequired", new TableInfo.Column("levelRequired", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("damage", new TableInfo.Column("damage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("defense", new TableInfo.Column("defense", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("shieldBonus", new TableInfo.Column("shieldBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("energyBonus", new TableInfo.Column("energyBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("strengthBonus", new TableInfo.Column("strengthBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("dexterityBonus", new TableInfo.Column("dexterityBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("intelligenceBonus", new TableInfo.Column("intelligenceBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("psionicsBonus", new TableInfo.Column("psionicsBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("technologyBonus", new TableInfo.Column("technologyBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("lore", new TableInfo.Column("lore", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("origin", new TableInfo.Column("origin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("creatorId", new TableInfo.Column("creatorId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEquipment.put("equipped", new TableInfo.Column("equipped", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEquipment = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEquipment = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEquipment = new TableInfo("Equipment", _columnsEquipment, _foreignKeysEquipment, _indicesEquipment);
        final TableInfo _existingEquipment = TableInfo.read(db, "Equipment");
        if (!_infoEquipment.equals(_existingEquipment)) {
          return new RoomOpenHelper.ValidationResult(false, "Equipment(com.cosmicodyssey.rpg.models.Equipment).\n"
                  + " Expected:\n" + _infoEquipment + "\n"
                  + " Found:\n" + _existingEquipment);
        }
        final HashMap<String, TableInfo.Column> _columnsMount = new HashMap<String, TableInfo.Column>(14);
        _columnsMount.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("species", new TableInfo.Column("species", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("levelRequired", new TableInfo.Column("levelRequired", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("speed", new TableInfo.Column("speed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("cargoCapacity", new TableInfo.Column("cargoCapacity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("combatBonus", new TableInfo.Column("combatBonus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("lore", new TableInfo.Column("lore", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("origin", new TableInfo.Column("origin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("creatorId", new TableInfo.Column("creatorId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMount.put("owned", new TableInfo.Column("owned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMount = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMount = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMount = new TableInfo("Mount", _columnsMount, _foreignKeysMount, _indicesMount);
        final TableInfo _existingMount = TableInfo.read(db, "Mount");
        if (!_infoMount.equals(_existingMount)) {
          return new RoomOpenHelper.ValidationResult(false, "Mount(com.cosmicodyssey.rpg.models.Mount).\n"
                  + " Expected:\n" + _infoMount + "\n"
                  + " Found:\n" + _existingMount);
        }
        final HashMap<String, TableInfo.Column> _columnsParty = new HashMap<String, TableInfo.Column>(14);
        _columnsParty.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("gameMasterType", new TableInfo.Column("gameMasterType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("difficulty", new TableInfo.Column("difficulty", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("hostId", new TableInfo.Column("hostId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("currentPlanet", new TableInfo.Column("currentPlanet", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("currentSystem", new TableInfo.Column("currentSystem", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("storyPrompt", new TableInfo.Column("storyPrompt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("storyHistory", new TableInfo.Column("storyHistory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("lastActivity", new TableInfo.Column("lastActivity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("partyCode", new TableInfo.Column("partyCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysParty = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesParty = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoParty = new TableInfo("Party", _columnsParty, _foreignKeysParty, _indicesParty);
        final TableInfo _existingParty = TableInfo.read(db, "Party");
        if (!_infoParty.equals(_existingParty)) {
          return new RoomOpenHelper.ValidationResult(false, "Party(com.cosmicodyssey.rpg.models.Party).\n"
                  + " Expected:\n" + _infoParty + "\n"
                  + " Found:\n" + _existingParty);
        }
        final HashMap<String, TableInfo.Column> _columnsPlanet = new HashMap<String, TableInfo.Column>(17);
        _columnsPlanet.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("systemId", new TableInfo.Column("systemId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("biome", new TableInfo.Column("biome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("dangerLevel", new TableInfo.Column("dangerLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("resourceLevel", new TableInfo.Column("resourceLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("mapImageUrl", new TableInfo.Column("mapImageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("discovered", new TableInfo.Column("discovered", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("visited", new TableInfo.Column("visited", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("x", new TableInfo.Column("x", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("y", new TableInfo.Column("y", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("atmosphere", new TableInfo.Column("atmosphere", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("gravity", new TableInfo.Column("gravity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("temperature", new TableInfo.Column("temperature", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlanet.put("dominantFaction", new TableInfo.Column("dominantFaction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlanet = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlanet = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlanet = new TableInfo("Planet", _columnsPlanet, _foreignKeysPlanet, _indicesPlanet);
        final TableInfo _existingPlanet = TableInfo.read(db, "Planet");
        if (!_infoPlanet.equals(_existingPlanet)) {
          return new RoomOpenHelper.ValidationResult(false, "Planet(com.cosmicodyssey.rpg.models.Planet).\n"
                  + " Expected:\n" + _infoPlanet + "\n"
                  + " Found:\n" + _existingPlanet);
        }
        final HashMap<String, TableInfo.Column> _columnsRule = new HashMap<String, TableInfo.Column>(10);
        _columnsRule.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("content", new TableInfo.Column("content", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("authorId", new TableInfo.Column("authorId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("isOfficial", new TableInfo.Column("isOfficial", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRule.put("pageNumber", new TableInfo.Column("pageNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRule = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRule = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRule = new TableInfo("Rule", _columnsRule, _foreignKeysRule, _indicesRule);
        final TableInfo _existingRule = TableInfo.read(db, "Rule");
        if (!_infoRule.equals(_existingRule)) {
          return new RoomOpenHelper.ValidationResult(false, "Rule(com.cosmicodyssey.rpg.models.Rule).\n"
                  + " Expected:\n" + _infoRule + "\n"
                  + " Found:\n" + _existingRule);
        }
        final HashMap<String, TableInfo.Column> _columnsSpaceship = new HashMap<String, TableInfo.Column>(20);
        _columnsSpaceship.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("model", new TableInfo.Column("model", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("levelRequired", new TableInfo.Column("levelRequired", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("speed", new TableInfo.Column("speed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("hull", new TableInfo.Column("hull", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("maxHull", new TableInfo.Column("maxHull", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("shields", new TableInfo.Column("shields", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("maxShields", new TableInfo.Column("maxShields", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("weaponSlots", new TableInfo.Column("weaponSlots", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("cargoCapacity", new TableInfo.Column("cargoCapacity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("crewCapacity", new TableInfo.Column("crewCapacity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("jumpRange", new TableInfo.Column("jumpRange", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("lore", new TableInfo.Column("lore", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("origin", new TableInfo.Column("origin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("creatorId", new TableInfo.Column("creatorId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpaceship.put("owned", new TableInfo.Column("owned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpaceship = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpaceship = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpaceship = new TableInfo("Spaceship", _columnsSpaceship, _foreignKeysSpaceship, _indicesSpaceship);
        final TableInfo _existingSpaceship = TableInfo.read(db, "Spaceship");
        if (!_infoSpaceship.equals(_existingSpaceship)) {
          return new RoomOpenHelper.ValidationResult(false, "Spaceship(com.cosmicodyssey.rpg.models.Spaceship).\n"
                  + " Expected:\n" + _infoSpaceship + "\n"
                  + " Found:\n" + _existingSpaceship);
        }
        final HashMap<String, TableInfo.Column> _columnsStarSystem = new HashMap<String, TableInfo.Column>(11);
        _columnsStarSystem.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("starType", new TableInfo.Column("starType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("x", new TableInfo.Column("x", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("y", new TableInfo.Column("y", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("discovered", new TableInfo.Column("discovered", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("connected", new TableInfo.Column("connected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("faction", new TableInfo.Column("faction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStarSystem.put("threatLevel", new TableInfo.Column("threatLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStarSystem = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStarSystem = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStarSystem = new TableInfo("StarSystem", _columnsStarSystem, _foreignKeysStarSystem, _indicesStarSystem);
        final TableInfo _existingStarSystem = TableInfo.read(db, "StarSystem");
        if (!_infoStarSystem.equals(_existingStarSystem)) {
          return new RoomOpenHelper.ValidationResult(false, "StarSystem(com.cosmicodyssey.rpg.models.StarSystem).\n"
                  + " Expected:\n" + _infoStarSystem + "\n"
                  + " Found:\n" + _existingStarSystem);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c0b3caad3dbd89f4a089b108b476f74f", "6606c029cbe3f71ed687ef6edf74e574");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Character","Equipment","Mount","Party","Planet","Rule","Spaceship","StarSystem");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Character`");
      _db.execSQL("DELETE FROM `Equipment`");
      _db.execSQL("DELETE FROM `Mount`");
      _db.execSQL("DELETE FROM `Party`");
      _db.execSQL("DELETE FROM `Planet`");
      _db.execSQL("DELETE FROM `Rule`");
      _db.execSQL("DELETE FROM `Spaceship`");
      _db.execSQL("DELETE FROM `StarSystem`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }
}
