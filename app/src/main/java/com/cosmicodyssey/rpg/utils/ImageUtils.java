package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {
    public static String copyUriToInternalStorage(Context context, Uri uri, String fileName) {
        try {
            File destFile = new File(context.getFilesDir(), fileName);
            InputStream in = context.getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
