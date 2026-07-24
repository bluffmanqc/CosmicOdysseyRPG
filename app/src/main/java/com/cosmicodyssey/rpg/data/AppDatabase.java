package com.cosmicodyssey.rpg.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Party;
import com.cosmicodyssey.rpg.models.Planet;
import com.cosmicodyssey.rpg.models.Rule;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.cosmicodyssey.rpg.models.StarSystem;

@Database(entities = {
    Character.class,
    Equipment.class,
    Mount.class,
    Party.class,
    Planet.class,
    Rule.class,
    Spaceship.class,
    StarSystem.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cosmic_odyssey_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
