package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageCacheManager {
    private final LruCache<String, Bitmap> memoryCache;
    private final File diskCacheDir;

    public ImageCacheManager(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        diskCacheDir = new File(context.getCacheDir(), "images");
        if (!diskCacheDir.exists()) diskCacheDir.mkdirs();
    }

    public void put(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) return;
        memoryCache.put(url, bitmap);
        saveToDisk(url, bitmap);
    }

    public Bitmap get(String url) {
        if (url == null) return null;
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) return bitmap;
        return loadFromDisk(url);
    }

    public Bitmap getAvatarFromPath(String path) {
        if (path == null) return null;
        File file = new File(path);
        if (file.exists()) {
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }

    private void saveToDisk(String url, Bitmap bitmap) {
        try {
            File file = new File(diskCacheDir, hashKey(url));
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadFromDisk(String url) {
        try {
            File file = new File(diskCacheDir, hashKey(url));
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String hashKey(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(key.hashCode());
        }
    }
}
