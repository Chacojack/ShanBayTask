package task.jack.me.shanbay.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

/**
 * Created by zjchai on 2016/12/10.
 */

public class FileUtils {

    public static String getTextFromInputStream(InputStream inputStream){
        InputStream is = inputStream;
        InputStreamReader isr = null;
        BufferedReader buffreader = null;
        try {
            StringBuilder buffer = new StringBuilder();

            isr = new InputStreamReader(is);
            buffreader = new BufferedReader(isr);
            String line;
            while ((line = buffreader.readLine()) != null) {
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
            if (buffreader != null) {
                try {
                    buffreader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffreader = null;
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
