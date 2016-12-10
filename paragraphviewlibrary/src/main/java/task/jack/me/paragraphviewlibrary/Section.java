package task.jack.me.paragraphviewlibrary;

import android.graphics.Rect;

/**
 * Created by zjchai on 2016/12/10.
 */

public class Section {

    private String content;
    private Rect bounds;
    private boolean selected = false;
    private boolean isRectified = false;

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

    public boolean isSelected() {
        return selected;
    }

    public Section setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public Section rectifyBounds(int x, int y) {
        if (isRectified) {
            return this;
        }
        isRectified = true;
        bounds.offset(x, y);
        return this;
    }
}
