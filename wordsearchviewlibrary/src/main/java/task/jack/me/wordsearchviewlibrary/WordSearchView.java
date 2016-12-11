package task.jack.me.wordsearchviewlibrary;

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

import task.jack.me.awesomefontviewlibrary.AwesomeFontView;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract.IWordSearchPresenter;
import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;
import task.jack.me.wordsearchviewlibrary.presenter.WordSearchPresenter;
import task.jack.me.wordseatchviewlibrary.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by zjchai on 2016/12/11.
 */

public class WordSearchView extends RelativeLayout implements WordSearchContract.IWordSearchView {

    private static final String TAG = WordSearchView.class.getSimpleName();

    /**
     *
     */
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

    public static WordSearchView getDefault(@NonNull Context context) {
        WordSearchView wordSearchView = new WordSearchView(context);
        wordSearchView.setBackgroundColor(Color.WHITE);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.word_search_root_padding);
        wordSearchView.setPadding(padding, padding, padding, padding);
        ViewCompat.setElevation(wordSearchView, 30);
        return wordSearchView;
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

    private ViewGroup.LayoutParams createLayoutParams(@NonNull ViewGroup container) {
        Log.d(TAG, "createLayoutParams() called with: container = [" + container + "], container height = [" + container.getHeight() + "]");
        ViewGroup.MarginLayoutParams params = new RelativeLayout.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.setMargins(0, container.getHeight(), 0, 0);
        return params;
    }

    public WordSearchView search(String word) {
        presenter.search(word);
        return this;
    }

    @Override
    public void showWordSearchInfo(@NonNull WordSearchInfo data) {
        Log.d(TAG, "showWordSearchInfo() called with: data = [" + data + "]");
        wordSearchInfo = data;
        contentTxt.setText(data.getContent());
        pronunciationTxt.setText(data.getPronunciation());
        definitionTxt.setText(data.getDefinition());
    }

    public void show() {
        if (needShow) {
            return;
        }
        needShow = true;
        requestLayout();
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
        showAnimator.start();
    }


    private void ensureEnterAnimator() {
        if (showAnimator == null) {
            showAnimator = new ObjectAnimator();
            showAnimator.setDuration(300);
            showAnimator.setPropertyName("Y");
            showAnimator.setTarget(this);
        }
    }

    public void hide() {
        startHideAnimator();
    }

    private void startHideAnimator() {
        ensureHideAnimator();
        if (hideAnimator.isRunning()) {
            hideAnimator.cancel();
        }
        hideAnimator.setFloatValues(getY(), container.getHeight());
        hideAnimator.start();
    }

    private void ensureHideAnimator() {
        if (hideAnimator == null) {
            hideAnimator = new ObjectAnimator();
            hideAnimator.setDuration(300);
            hideAnimator.setPropertyName("Y");
            hideAnimator.setTarget(this);
        }
    }

}








































