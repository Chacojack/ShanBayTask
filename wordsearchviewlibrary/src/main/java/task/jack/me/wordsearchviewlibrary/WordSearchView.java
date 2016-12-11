package task.jack.me.wordsearchviewlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import task.jack.me.awesomefontviewlibrary.AwesomeFontView;
import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;
import task.jack.me.wordseatchviewlibrary.R;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract.IWordSearchPresenter;
import task.jack.me.wordsearchviewlibrary.presenter.WordSearchPresenter;

/**
 * Created by zjchai on 2016/12/11.
 */

public class WordSearchView extends RelativeLayout implements WordSearchContract.IWordSearchView {

    private static final String TAG = WordSearchView.class.getSimpleName();

    private String audioOn;
    private String audioOff;

    private WordSearchInfo wordSearchInfo;

    private TextView contentTxt;
    private TextView pronunciationTxt;
    private TextView definitionTxt;
    private AwesomeFontView audioBtn;

    private IWordSearchPresenter presenter;


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

    private void init(){
        inflate(getContext(), R.layout.view_word_search,this);
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
        audioBtn.setText(audioOff);
        audioBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                audioBtn.setText(audioOn);
                presenter.playAudio(wordSearchInfo.getAudioUrl());
            }
        });
    }

    public WordSearchView search(String word){
        presenter.search(word);
        return this;
    }

    @Override
    public void showWordSearchInfo(@NonNull WordSearchInfo data) {
        Log.d(TAG, "showWordSearchInfo() called with: data = [" + data + "]");
        if (data.equals(wordSearchInfo)) {
            return;
        }
        wordSearchInfo = data;
        contentTxt.setText(data.getContent());
        pronunciationTxt.setText(data.getPronunciation());
        definitionTxt.setText(data.getDefinition());
    }

    @Override
    public void resetAudioBtn() {
        audioBtn.setText(audioOff);
    }
}
