package task.jack.me.wordsearchviewlibrary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import task.jack.me.awesomefontviewlibrary.AwesomeFontView;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract.IWordSearchPresenter;
import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;
import task.jack.me.wordsearchviewlibrary.presenter.WordSearchPresenter;
import task.jack.me.wordseatchviewlibrary.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 查词框
 *
 * 可以显示查找出的词的，文本，释义，音标。还可以播放发音
 * 使用方法：
 *      1.XML静态使用：可以按照正常的View在布局文件中使用。
 *      2.Java动态使用：针对Java代码动态添加使用提供了简单易用，可以链式调用的方法。
 *          首先，可以通过{@link WordSearchView}的静态方法{@link WordSearchView#getDefault(Context)}
 *              得到一个默认的查词框实例。
 *          然后，通过{@link WordSearchView#setContainer(ViewGroup)}方法为查词框设置一个容器，推荐使用
 *              {@link android.widget.FrameLayout}做为容器。
 *          然后，通过{@link WordSearchView#search(String)}方法设置要查询的单词。
 *          之后，可以通过{@link WordSearchView#show()}方法将界面从容器底部展示出来。
 *          最后，通过{@link WordSearchView#hide()}方法将界面隐藏收回底部。
 */
public class WordSearchView extends RelativeLayout implements WordSearchContract.IWordSearchView {

    private static final String TAG = WordSearchView.class.getSimpleName();

    private boolean needShow = false;

    private String audioOn;
    private String audioOff;

    private WordSearchInfo wordSearchInfo;

    private TextView contentTxt;
    private TextView pronunciationTxt;
    private TextView definitionTxt;
    private AwesomeFontView audioBtn;
    private ViewGroup container;

    private IWordSearchPresenter presenter;
    private ObjectAnimator showAnimator;
    private ObjectAnimator hideAnimator;
    private ObjectAnimator lastestAnimator;


    public WordSearchView(Context context) {
        this(context, null);
    }

    public WordSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        initViews();
        afterViews();
    }

    /**
     * 获得默认设置的查词框，注意：多次调用会获得不同的实例。
     *
     * @param context {@link Context}，注意：不能为空后果自负
     * @return 一个查词框实例
     */
    public static WordSearchView getDefault(@NonNull Context context) {
        WordSearchView wordSearchView = new WordSearchView(context);
        wordSearchView.setBackgroundColor(Color.WHITE);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.word_search_root_padding);
        wordSearchView.setPadding(padding, padding, padding, padding);
        ViewCompat.setElevation(wordSearchView, 30);
        return wordSearchView;
    }

    /**
     * 设置查词框容器
     *
     * @param container 容器
     * @return 这个查词框
     */
    public WordSearchView setContainer(ViewGroup container) {
        if (this.container == container) {
            return this;
        }
        if (this.container != null) {
            this.container.removeView(this);
        }
        this.container = container;
        addToContainer();
        return this;
    }

    /**
     * 查询单词
     *
     * @param word 要查询的单词
     * @return 这个查词框
     */
    public WordSearchView search(String word) {
        presenter.search(word);
        return this;
    }

    /**
     * 从底部显示查词框，会使用默认的动画
     */
    public void show() {
        if (needShow) {
            return;
        }
        needShow = true;
        requestLayout();
    }

    /**
     * 从底部隐藏查词框，会使用默认的动画
     */
    public void hide() {
        startHideAnimator();
    }

    private void init() {
        inflate(getContext(), R.layout.view_word_search, this);
        presenter = new WordSearchPresenter(this);
        audioOn = getResources().getString(R.string.audio_on);
        audioOff = getResources().getString(R.string.audio_off);
    }

    private void initViews() {
        contentTxt = (TextView) findViewById(R.id.txt_content);
        pronunciationTxt = (TextView) findViewById(R.id.txt_pronunciation);
        definitionTxt = (TextView) findViewById(R.id.txt_definition);
        audioBtn = (AwesomeFontView) findViewById(R.id.btn_audio);
    }

    private void afterViews() {
        if (wordSearchInfo != null) {
            showWordSearchInfo(wordSearchInfo);
        }
        audioBtn.setText(audioOff);
        audioBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                audioBtn.setText(audioOn);
                presenter.playAudio(wordSearchInfo.getAudioUrl());
            }
        });
    }

    private void addToContainer() {
        if (container == null) {
            return;
        }
        container.addView(this);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = MATCH_PARENT;
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.default_word_search_view_height);
        if (layoutParams instanceof MarginLayoutParams) {
            ((MarginLayoutParams) layoutParams).setMargins(0, container.getHeight(), 0, 0);
        }
    }


    @Override
    public void showWordSearchInfo(@NonNull WordSearchInfo data) {
        Log.d(TAG, "showWordSearchInfo() called with: data = [" + data + "]");
        wordSearchInfo = data;
        contentTxt.setText(data.getContent());
        pronunciationTxt.setText(data.getPronunciation());
        definitionTxt.setText(data.getDefinition());
    }



    @Override
    public void resetAudioBtn() {
        audioBtn.setText(audioOff);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        checkNeedShowAnimator();
    }

    @Override
    public void alarmSearchFailMsg(String msg) {
        hide();
        Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void alarmSearchFailDefaultMsg() {
        alarmSearchFailMsg(getResources().getString(R.string.default_search_fail_msg));
    }

    private void checkNeedShowAnimator() {
        if (needShow) {
            needShow = false;
            startShowAnimator();
        }
    }

    private void startShowAnimator() {
        Log.d(TAG, "startShowAnimator() called translationY:" + getTranslationY() + ",getHeight:" + getHeight());
        ensureEnterAnimator();
        if (showAnimator.isRunning()) {
            showAnimator.cancel();
        }
        float start = getY();
        float end = container.getHeight() - getHeight();
        if (start == end) {
            return;
        }
        showAnimator.setFloatValues(start, end);
        startAnimator(showAnimator);
    }

    private void ensureEnterAnimator() {
        if (showAnimator == null) {
            showAnimator = new ObjectAnimator();
            showAnimator.setDuration(300);
            showAnimator.setPropertyName("Y");
            showAnimator.setTarget(this);
        }
    }

    private void startHideAnimator() {
        ensureHideAnimator();
        if (hideAnimator.isRunning()) {
            hideAnimator.cancel();
        }
        hideAnimator.setFloatValues(getY(), container.getHeight());
        startAnimator(hideAnimator);
    }

    private void ensureHideAnimator() {
        if (hideAnimator == null) {
            hideAnimator = new ObjectAnimator();
            hideAnimator.setDuration(300);
            hideAnimator.setPropertyName("Y");
            hideAnimator.setTarget(this);
        }
    }

    private void startAnimator(ObjectAnimator animator){
        if (lastestAnimator != null && lastestAnimator.isRunning()) {
            lastestAnimator.cancel();
        }
        lastestAnimator = animator;
        lastestAnimator.start();
    }

}








































