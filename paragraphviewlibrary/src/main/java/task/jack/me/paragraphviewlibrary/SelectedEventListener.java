package task.jack.me.paragraphviewlibrary;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by zjchai on 2016/12/11.
 */

public interface SelectedEventListener {

    int SELECTED = 1;
    int CANCEL = 2;

    @IntDef({SELECTED, CANCEL})
    @interface SelectedEventType {
    }

    /**
     * 当选中或者取消选中一个单词的时候触发
     *
     * @param type 事件类型
     * @param word 事件触发的单词，可能为空
     */
    void onSelectedEvent(@SelectedEventType int type, @Nullable String word);


}
