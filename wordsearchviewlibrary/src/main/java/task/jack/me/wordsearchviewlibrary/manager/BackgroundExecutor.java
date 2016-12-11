package task.jack.me.wordsearchviewlibrary.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zjchai on 2016/12/11.
 */

public class BackgroundExecutor {

    private static ExecutorService executorService;

    private static void ensureExecutor() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public static void execute(Runnable runnable){
        ensureExecutor();
        executorService.execute(runnable);
    }
}
