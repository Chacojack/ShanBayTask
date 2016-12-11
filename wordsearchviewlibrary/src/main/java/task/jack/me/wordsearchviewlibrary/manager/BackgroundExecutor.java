package task.jack.me.wordsearchviewlibrary.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zjchai on 2016/12/11.
 */

public class BackgroundExecutor {

    private static ExecutorService executorService;
    private static Future future;

    public static void execute(Runnable runnable) {
        ensureExecutor();
        cancelCurrentThread();
        future = executorService.submit(runnable);
    }

    private static void ensureExecutor() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    private static void cancelCurrentThread() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
}
