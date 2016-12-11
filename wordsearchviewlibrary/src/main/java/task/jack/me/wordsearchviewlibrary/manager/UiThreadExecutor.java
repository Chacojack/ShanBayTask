package task.jack.me.wordsearchviewlibrary.manager;

import android.os.Handler;

/**
 * Created by zjchai on 2016/12/11.
 */

public class UiThreadExecutor {

    private static Handler HANDLER = new Handler();

    public static void execute(Runnable runnable){
        HANDLER.post(runnable);
    }

}
