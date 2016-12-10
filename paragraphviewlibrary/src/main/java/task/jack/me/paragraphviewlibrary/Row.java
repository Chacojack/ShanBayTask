package task.jack.me.paragraphviewlibrary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by zjchai on 2016/12/10.
 */

public class Row {

    private List<Section> sections = new ArrayList<>();
    private int realWidth = 0;
    private boolean isTail;

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public int getRealWidth() {
        return realWidth;
    }

    public Row setRealWidth(int realWidth) {
        this.realWidth = realWidth;
        return this;
    }

    public Row addSection(Section section){
        sections.add(section);
        realWidth += section.getBounds().width();
        return this;
    }

    public boolean isTail() {
        return isTail;
    }

    public Row setTail(boolean tail) {
        isTail = tail;
        return this;
    }
}
