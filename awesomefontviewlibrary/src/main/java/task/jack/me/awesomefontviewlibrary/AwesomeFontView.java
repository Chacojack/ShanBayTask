package task.jack.me.awesomefontviewlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zjchai on 2016/11/3.
 */

public class AwesomeFontView extends TextView {

    public AwesomeFontView(Context context) {
        this(context, null);
    }

    public AwesomeFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        obtainAttr(context, attrs);
        afterViews();
    }

    private void obtainAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AwesomeFontView);
        int color = typedArray.getColor(R.styleable.AwesomeFontView_pressableTextColor, Color.BLACK);
        setTextColor(getColorStateList(color));
        typedArray.recycle();
    }

    private void afterViews() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
        setTypeface(typeface);
        setIncludeFontPadding(false);
    }

    private ColorStateList getColorStateList(int color) {
        int pressedColor = (color & 0xffffff) | 0x77000000;
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_pressed},
                new int[]{android.R.attr.state_pressed}
        };
        int[] colors = new int[]{color, pressedColor};

        return new ColorStateList(states, colors);
    }


}
