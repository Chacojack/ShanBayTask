package task.jack.me.wordsearchviewlibrary.manager;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import task.jack.me.wordsearchviewlibrary.manager.protocol.WordSearchResult;

/**
 * Created by zjchai on 2016/12/11.
 */

public class HttpManager {

    private static final String TAG = HttpManager.class.getSimpleName();

    public static final String SEARCH_WORD_URL = "https://api.shanbay.com/bdc/search/?word=%s";

    public static WordSearchResult searchWord(String word){
        return getObject(String.format(SEARCH_WORD_URL, word), WordSearchResult.class);
    }

    public static <T> T getObject(String httpUrl, Class<T> clazz) {
        String string = getString(httpUrl);
        if (string != null) {
            return new Gson().fromJson(string, clazz);
        } else {
            return null;
        }
    }

    public static String getString(String httpUrl) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[1024];
            int length;
            while ((length = bufferedReader.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, length));
            }
            Log.d(TAG, "getString() returned: " + builder.toString());
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
