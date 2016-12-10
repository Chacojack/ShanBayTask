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
    private Rect selectedBounds;
    private int selectedStart;
    private int selectedEnd;

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

    public Rect getSelectedBounds() {
        return selectedBounds;
    }

    public Section setSelectedBounds(Rect selectedBounds) {
        this.selectedBounds = selectedBounds;
        return this;
    }

    public int getSelectedStart() {
        return selectedStart;
    }

    public Section setSelectedStart(int selectedStart) {
        this.selectedStart = selectedStart;
        return this;
    }

    public int getSelectedEnd() {
        return selectedEnd;
    }

    public Section setSelectedEnd(int selectedEnd) {
        this.selectedEnd = selectedEnd;
        return this;
    }
}
