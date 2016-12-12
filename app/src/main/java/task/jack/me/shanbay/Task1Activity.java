package task.jack.me.shanbay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.internal.schedulers.ExecutorScheduler;
import rx.schedulers.Schedulers;
import task.jack.me.paragraphviewlibrary.ParagraphView;
import task.jack.me.shanbay.utils.FileUtils;
import task.jack.me.wordsearchviewlibrary.WordSearchView;

import static task.jack.me.paragraphviewlibrary.SelectedEventListener.CANCEL;
import static task.jack.me.paragraphviewlibrary.SelectedEventListener.SELECTED;

/**
 * 完成作业一的Activity
 * <p>
 * 要求：
 * 1. 渲染一段英文文本                                           OK
 * 2. 文本中的单词，被点击后可以高亮                               OK
 * 3. 单词高亮后，显示一个查词框(不限查词框形式)  查词框的内容        OK
 * a. 单词文本                                                  OK
 * b. 单词释义                                                  OK
 * c. 单词音标                                                  OK
 * d. 点击可以播放单词发音请通过扇贝网的开放API获取这些内容点击查看    OK
 * 4. 把这个查词框做成一个独立的控件(合理封装，接口友好，应用多种场景){@link WordSearchView}
 * 5. 可以参考扇贝新闻的效果                                      OK
 * 6. 合理的注释方便代码阅读                                      OK
 * <p>
 * 进阶：
 * 1. 通过自定义View实现该控件{@link ParagraphView}               OK
 * 2. 文本实现两边对齐并且同样支持点击查词                          OK
 * 3. 给查词框增加出现和消失的动画或者合理的手势操作 [因为和ScrollView的滚动有冲突，之后考虑怎样的手势合理]
 */
public class Task1Activity extends AppCompatActivity {

    private static final String TAG = Task1Activity.class.getSimpleName();
    @BindView(R.id.paragraph_view)
    ParagraphView paragraphView;
    @BindView(R.id.root_fl)
    FrameLayout rootRl;

    WordSearchView wordSearchView;

    public static void start(Context context) {
        Intent intent = new Intent(context, Task1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);
        ButterKnife.bind(this);
        afterViews();
    }

    private void afterViews() {
        initParagraphView();
        loadContent();
    }

    private void loadContent() {
        Schedulers.io().createWorker().schedule(() -> {
            String content = FileUtils.getTextFromAsset(getAssets(), "news");
            AndroidSchedulers.mainThread().createWorker().schedule(() -> paragraphView.setContent(content));
        });
    }

    private void initParagraphView() {
        paragraphView.setSelectedEventListener((type, word) -> {
            Log.d(TAG, "onSelectedEvent() called with: type = [" + type + "], word = [" + word + "]");
            switch (type) {
                case SELECTED:
                    ensureWordSearchView();
                    wordSearchView.setContainer(rootRl).search(word).show();
                    break;
                case CANCEL:
                    ensureWordSearchView();
                    wordSearchView.hide();
                    break;
            }
        });
    }

    private void ensureWordSearchView() {
        if (wordSearchView == null) {
            wordSearchView = WordSearchView.getDefault(this);
        }
    }
}
