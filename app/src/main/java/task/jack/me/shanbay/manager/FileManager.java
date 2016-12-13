package task.jack.me.shanbay.manager;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by zjchai on 2016/12/13.
 */

public class FileManager {

    private static final String TAG = FileManager.class.getSimpleName();

    public static final String DIR_IMAGE_NAME = "Images";


    public static String getImageDirPath(Context context) {
        String result;
        File file = context.getExternalFilesDir(null);
        if (file != null) {
            result = file.getParentFile().getAbsolutePath() + "/Images/";
        } else {
            result = context.getFilesDir().getParentFile().getAbsolutePath() + "/Images/";
        }

        File dir = new File(result);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Log.d(TAG, "getImageDirPath() returned: " + result);
        return result;
    }

    public static String getImageLocalPath(Context context, String name) {
        return getImageDirPath(context) + name;
    }

}
