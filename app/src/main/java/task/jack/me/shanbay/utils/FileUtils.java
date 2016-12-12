package task.jack.me.shanbay.utils;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
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
        InputStream is = inputStream;
        InputStreamReader isr = null;
        BufferedReader bufferReader = null;
        try {
            StringBuilder buffer = new StringBuilder();

            isr = new InputStreamReader(is);
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
                isr = null;
            }
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bufferReader = null;
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
        return null;
    }

}
