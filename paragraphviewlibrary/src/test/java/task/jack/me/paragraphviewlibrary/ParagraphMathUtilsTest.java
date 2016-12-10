package task.jack.me.paragraphviewlibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zjchai on 2016/12/10.
 */
public class ParagraphMathUtilsTest {
    @Test
    public void getEnglishWord() throws Exception {
        String englishWord = ParagraphMathUtils.getEnglishWord("good,");
        assertEquals(englishWord, "good");

        String englishWord1 = ParagraphMathUtils.getEnglishWord("\"good");
        assertEquals(englishWord1, "good");
    }

}