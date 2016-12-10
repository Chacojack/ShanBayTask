package task.jack.me.paragraphviewlibrary;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于提供计算的工具类
 * <p>
 * Created by zjchai on 2016/12/10.
 */
public class ParagraphMathUtils {

    public static String[] divideToParagraph(@NonNull String content) {
        return content.split("\n");
    }

    public static String[] divideToWord(@NonNull String paragraph) {
        return paragraph.split(" ");
    }

    public static List<Row> divideToLine(@NonNull Paint paint, @NonNull String content
            , @IntRange(from = 0) int lineWidth) {
        // 1.init result array
        List<Row> result = new ArrayList<>();
        result.add(new Row());

        // 2.init textPaint, blank bounds and current width
        int blankWidth = getBlankWidth(paint);

        // 3.divide words and divide to lines
        String[] paragraphs = divideToParagraph(content);
        for (int i = 0; i < paragraphs.length; i++) {
            String[] strings = divideToWord(paragraphs[i]);
            int currentWidth = -blankWidth;
            for (int j = 0; j < strings.length; j++) {
                String line = strings[j];
                Rect bounds = getWordBounds(paint, line);
                if (currentWidth + bounds.width() + blankWidth > lineWidth) {
                    currentWidth = -blankWidth;
                    result.add(new Row());
                }
                currentWidth += bounds.width() + blankWidth;
                result.get(result.size() - 1)
                        .addSection(new Section().setContent(line).setBounds(bounds));
            }
            result.get(result.size() - 1).setTail(true);
            result.add(new Row());
            result.get(result.size() - 1).addSection(new Section().setContent("\n").setBounds(new Rect()));
            result.add(new Row());
        }
        return result;
    }

    public static int getBlankWidth(@NonNull Paint paint) {
        return getWordWidth(paint, "a a") - getWordWidth(paint, "aa");
    }

    public static int getWordWidth(@NonNull Paint paint, @NonNull String content) {
        return getWordBounds(paint, content).width();
    }

    public static Rect getWordBounds(@NonNull Paint paint, @NonNull String content) {
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        return bounds;
    }

    public static int getLineHeight(@NonNull Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.bottom - fontMetrics.top);
    }

    public static String getEnglishWord(@NonNull String content) {
        Matcher matcher = getEnglishWordMatcher(content);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @NonNull
    public static Matcher getEnglishWordMatcher(@NonNull String content) {
        Pattern pattern = Pattern.compile("^*[A-Za-z]+");
        return pattern.matcher(content);
    }


    public static boolean checkTouchEnglishWord(@NonNull Paint paint, @NonNull Section section
            , @NonNull MotionEvent event) {
        Rect bounds = section.getBounds();
        String content = section.getContent();
        Matcher matcher = getEnglishWordMatcher(section.getContent());
        Rect temp = new Rect();
        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();
            int preWidth = start > 0 ? getWordWidth(paint, content.substring(0, start)) : 0;
            int wordWidth = getWordWidth(paint, word);
            temp.set(bounds.left + preWidth, bounds.top, bounds.left + wordWidth + preWidth, bounds.bottom);
            if (temp.contains((int) event.getX(), (int) event.getY())) {
                section.setSelectedBounds(temp);
                section.setSelectedStart(start);
                section.setSelectedEnd(matcher.end());
                return true;
            }
        }
        return false;
    }


}























