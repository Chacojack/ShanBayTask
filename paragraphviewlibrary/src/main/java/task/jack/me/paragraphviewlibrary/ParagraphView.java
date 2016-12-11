package task.jack.me.paragraphviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
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
    private int rectColor = 0xFF3F51B5;
    private int textColor = 0xFF000000;
    private int textLightColor = 0xFFFFFFFF;
    private int rectLeftPadding = 4;
    private int rectTopPadding = 8;
    private int rectRightPadding = 5;
    private int rectBottomPadding = 8;
    private int padding = 0;

    private float rectRound = 8f;
    private float lineSize = 1f;

    private String content;

    private Paint textPaint;
    private Paint rectPaint;
    private List<Row> rows;
    private Section tempTouchSection;
    private Section touchSection;

    private SelectedEventListener selectedEventListener;

    public ParagraphView(Context context) {
        this(context, null);
    }

    public ParagraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParagraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        afterViews();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParagraphView);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.ParagraphView_textSize, this.textSize);
        setTextSize(textSize);
        lineSize = typedArray.getFloat(R.styleable.ParagraphView_lineSize, lineSize);
        textColor = typedArray.getColor(R.styleable.ParagraphView_textColor, textColor);
        textLightColor = typedArray.getColor(R.styleable.ParagraphView_textLightColor, textLightColor);
        rectColor = typedArray.getColor(R.styleable.ParagraphView_rectColor, rectColor);
        padding = typedArray.getDimensionPixelSize(R.styleable.ParagraphView_padding, padding);
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        textSize,
                        context.getResources().getDisplayMetrics()));
        textPaint.setColor(textColor);
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(rectColor);
    }

    public void setTextSize(int textSize) {
        if (this.textSize == textSize) {
            return;
        }
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
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

    public SelectedEventListener getSelectedEventListener() {
        return selectedEventListener;
    }

    public ParagraphView setSelectedEventListener(SelectedEventListener selectedEventListener) {
        this.selectedEventListener = selectedEventListener;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - padding * 2;
        if (content != null) {
            rows = ParagraphMathUtils.divideToLine(textPaint, content, widthSize);
            lineHeight = (int) (ParagraphMathUtils.getLineHeight(textPaint) * lineSize);
            int height = lineHeight * rows.size() + padding * 2;

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
        if (rows != null) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float y = padding - fontMetrics.top;
            for (Row row : rows) {
                drawRow(canvas, row, y);
                y += lineHeight;
            }
        }
    }

    private void drawRow(Canvas canvas, Row row, float y) {
        float x = padding;
        float blankWidth = 0;
        if (row.isTail()) {
            blankWidth = ParagraphMathUtils.getBlankWidth(textPaint);
        } else {
            if (row.getSections().size() > 1) {
                int realWidth = row.getRealWidth();
                blankWidth = (getWidth() - padding * 2 - realWidth) * 1f / (row.getSections().size() - 1);
            }
        }
        for (Section section : row.getSections()) {
            drawSection(canvas, section, x, y);
            x += section.getBounds().width() + blankWidth;
        }
    }

    private void drawSection(Canvas canvas, Section section, float x, float y) {
        section.rectifyBounds((int) x, (int) y);
        String content = section.getContent();
        if (section.isSelected()) {
            Rect selectedBounds = section.getSelectedBounds();
            int selectedStart = section.getSelectedStart();
            int selectedEnd = section.getSelectedEnd();
            RectF rectF = new RectF(selectedBounds.left - rectLeftPadding, selectedBounds.top - rectTopPadding
                    , selectedBounds.right + rectRightPadding, selectedBounds.bottom + rectBottomPadding);
            canvas.drawRoundRect(rectF, rectRound, rectRound, rectPaint);
            canvas.drawText(content, 0, selectedStart, x, y, textPaint);
            textPaint.setColor(textLightColor);
            canvas.drawText(content, selectedStart, selectedEnd, selectedBounds.left, y, textPaint);
            textPaint.setColor(textColor);
            canvas.drawText(content, selectedEnd, content.length(), rectF.right, y, textPaint);
        } else {
            canvas.drawText(content, x, y, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return handleTouchDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleTouchMove(event);
            case MotionEvent.ACTION_CANCEL:
                tempTouchSection = null;
                return false;
            case MotionEvent.ACTION_UP:
                return handleTouchUp(event);
            default:
                this.tempTouchSection = null;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean handleTouchUp(MotionEvent event) {
        if (tempTouchSection != null) {
            if (tempTouchSection.getSelectedBounds().contains((int) event.getX(), (int) event.getY())) {
                tempTouchSection.setSelected(true);
                onSelectedEvent(tempTouchSection);
            } else {
                tempTouchSection = null;
                onCancelSelectedEvent();
            }
        } else {
            onCancelSelectedEvent();
        }
        return false;
    }

    private boolean handleTouchMove(MotionEvent event) {
        if (tempTouchSection != null) {
            if (!tempTouchSection.getBounds().contains((int) event.getX(), (int) event.getY())) {
                tempTouchSection = null;
            }
        }
        return true;
    }

    private boolean handleTouchDown(MotionEvent event) {
        tempTouchSection = null;
        Section section = getTouchSection(event);
        if (section != null) {
            tempTouchSection = section;
        }
        return true;
    }

    private Section getTouchSection(MotionEvent event) {
        if (rows != null) {
            float x = event.getX();
            float y = event.getY();
            int rowNumber = (int) (y / lineHeight);
            if (rows.size() > rowNumber) {
                Row row = rows.get(rowNumber);
                for (Section section : row.getSections()) {
                    if (section.getBounds().contains((int) x, (int) y)) {
                        if (ParagraphMathUtils.checkTouchEnglishWord(textPaint, section, event)) {
                            return section;
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void onSelectedEvent(Section tempTouchSection) {
        if (touchSection != null) {
            touchSection.setSelected(false);
        }
        touchSection = tempTouchSection;
        touchSection.setSelected(true);
        invalidate();
        triggerSelectedEventListener(SelectedEventListener.SELECTED, touchSection);
    }

    private void triggerSelectedEventListener(@SelectedEventListener.SelectedEventType int type, Section touchSection) {
        if (selectedEventListener != null) {
            String word = null;
            if (touchSection != null) {
                String content = touchSection.getContent();
                int selectedStart = touchSection.getSelectedStart();
                int selectedEnd = touchSection.getSelectedEnd();
                word = content.substring(selectedStart, selectedEnd);
            }
            selectedEventListener.onSelectedEvent(type, word);
        }
    }

    private void onCancelSelectedEvent() {
        if (touchSection != null) {
            touchSection.setSelected(false);
            touchSection = null;
            invalidate();
            triggerSelectedEventListener(SelectedEventListener.CANCEL, touchSection);
        }
    }


}





































