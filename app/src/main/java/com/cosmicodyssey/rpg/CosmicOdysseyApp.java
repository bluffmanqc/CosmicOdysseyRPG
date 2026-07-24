package com.cosmicodyssey.rpg;

import android.app.Application;
import android.content.Context;

import com.cosmicodyssey.rpg.data.AppDatabase;
import com.cosmicodyssey.rpg.utils.ImageCacheManager;
import com.cosmicodyssey.rpg.utils.TTSManager;

public class CosmicOdysseyApp extends Application {
    private static CosmicOdysseyApp instance;
    private TTSManager ttsManager;
    private ImageCacheManager imageCacheManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase.getInstance(this);
        imageCacheManager = new ImageCacheManager(this);
        ttsManager = new TTSManager(this);
    }

    public static CosmicOdysseyApp getInstance() {
        return instance;
    }

    public TTSManager getTTSManager() {
        return ttsManager;
    }

    public ImageCacheManager getImageCacheManager() {
        return imageCacheManager;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
