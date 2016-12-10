package task.jack.me.paragraphviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 显示一段英文文字的View
 *
 * Created by zjchai on 2016/12/10.
 */
public class ParagraphView extends View {

    String content = "";

    public ParagraphView(Context context) {
        this(context, null);
    }

    public ParagraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParagraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        afterViews();
    }

    private void afterViews() {

    }

    public String getContent() {
        return content;
    }

    public ParagraphView setContent(String content) {
        this.content = content;
        return this;
    }









}
