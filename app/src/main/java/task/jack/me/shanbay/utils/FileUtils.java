package task.jack.me.shanbay.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

/**
 * Created by zjchai on 2016/12/10.
 */

public class FileUtils {


    public static String getTextFromAsset(@NonNull AssetManager assetManager, @NonNull String name) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(name);
            return FileUtils.getTextFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getTextFromInputStream(@NonNull InputStream inputStream) {
        InputStreamReader isr = null;
        BufferedReader bufferReader = null;
        try {
            StringBuilder buffer = new StringBuilder();

            isr = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(isr);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                buffer.append(line);
                buffer.append('\n');
            }
            return buffer.toString();
        } catch (IOException e) {
            Log.d(TAG, "readTextFile: has exception " + e.toString());
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveBitmap(@NonNull Bitmap bitmap, @NonNull String path, @NonNull CompressFormat format) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(format, 100, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getBitmapFullNameFromURL(@NonNull String url) {
        String[] strings = url.split("/");
        return strings[strings.length - 1];
    }

}









































