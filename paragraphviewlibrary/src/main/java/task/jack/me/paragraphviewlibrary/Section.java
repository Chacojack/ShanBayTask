package task.jack.me.paragraphviewlibrary;

import android.graphics.Rect;

/**
 * Created by zjchai on 2016/12/10.
 */

public class Section {

    private String content;
    private Rect bounds;

    public String getContent() {
        return content;
    }

    public Section setContent(String content) {
        this.content = content;
        return this;
    }

    public Rect getBounds() {
        return bounds;
    }

    public Section setBounds(Rect bounds) {
        this.bounds = bounds;
        return this;
    }

}
