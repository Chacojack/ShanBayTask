package task.jack.me.wordsearchviewlibrary.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by zjchai on 2016/12/11.
 */

public class UiThreadExecutor {

    private static Handler HANDLER = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            Runnable callback = msg.getCallback();
            if (callback != null) {
                callback.run();
            } else {
                super.handleMessage(msg);
            }
        }
    };

    public static void execute(Runnable runnable){
        HANDLER.post(runnable);
    }

}
