package task.jack.me.paragraphviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * 显示一段英文文字的View
 * <p>
 * Created by zjchai on 2016/12/10.
 */
public class ParagraphView extends View {

    private int textSize = 15;
    private int lineHeight;

    private float lineSize = 1f;

    private String content;

    private Paint textPaint;
    private List<Row> sections;


    public ParagraphView(Context context) {
        this(context, null);
    }

    public ParagraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParagraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        init(context, attrs, defStyle);
        afterViews();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParagraphView);
        textSize = typedArray.getDimensionPixelSize(R.styleable.ParagraphView_textSize, textSize);
        lineSize = typedArray.getFloat(R.styleable.ParagraphView_lineSize, lineSize);
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        textSize,
                        context.getResources().getDisplayMetrics()));
    }

    private void afterViews() {

    }

    public String getContent() {
        return content;
    }

    public ParagraphView setContent(String content) {
        if (this.content == content) {
            return this;
        }
        this.content = content;
        requestLayout();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (content != null) {
            sections = ParagraphMathUtils.divideToLine(textPaint, content, widthSize);
            lineHeight = (int) (ParagraphMathUtils.getLineHeight(textPaint) * lineSize);
            int height = lineHeight * sections.size();

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("ParagraphView", "onLayout: width : " + getWidth() + ", height: " + getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sections != null) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float y = -fontMetrics.top;
            for (Row row : sections) {
                float x = 0;
                float blankWidth = 0;
                if (row.isTail()) {
                    blankWidth = ParagraphMathUtils.getBlankWidth(textPaint);
                } else {
                    if (row.getSections().size() > 1) {
                        int realWidth = row.getRealWidth();
                        blankWidth = (getWidth() - realWidth) * 1f / (row.getSections().size() - 1);
                    }
                }
                for (Section section : row.getSections()) {
                    canvas.drawText(section.getContent(), x, y, textPaint);
                    x += section.getBounds().width() + blankWidth;
                }
                y += lineHeight;
            }
        }
    }

}





































