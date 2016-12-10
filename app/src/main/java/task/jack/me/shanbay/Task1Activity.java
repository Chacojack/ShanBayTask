package task.jack.me.shanbay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import task.jack.me.paragraphviewlibrary.ParagraphView;
import task.jack.me.shanbay.utils.FileUtils;

/**
 * 完成作业一的Activity
 * <p>
 * 要求：
 * 1. 渲染一段英文文本 OK
 * 2. 文本中的单词，被点击后可以高亮
 * 3. 单词高亮后，显示一个查词框(不限查词框形式)。 查词框的内容:
 * a. 单词文本 b. 单词释义 c. 单词音标
 * d. 点击可以播放单词发音 请通过扇贝网的开放API获取这些内容点击查看。
 * 4. 把这个查词框做成一个独立的控件(合理封装，接口友好，应用多种场景)
 * 5. 可以参考扇贝新闻的效果
 * 6. 合理的注释方便代码阅读
 * <p>
 * 进阶：
 * 1. 通过自定义View实现该控件 OK
 * 2. 文本实现两边对齐并且同样支持点击查词 OK
 * 3. 给查词框增加出现和消失的动画或者合理的手势操作
 */
public class Task1Activity extends AppCompatActivity {

    private static final String TAG = Task1Activity.class.getSimpleName();
    @BindView(R.id.paragraph_view)
    ParagraphView paragraphView;

    public static void start(Context context) {
        Intent intent = new Intent(context, Task1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);
        ButterKnife.bind(this);

        String content = null;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("news");
            content = FileUtils.getTextFromInputStream(inputStream);
            paragraphView.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
        }
    }
}
