package task.jack.me.wordsearchviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import task.jack.me.wordseatchviewlibrary.R;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract.IWordSearchPresenter;
import task.jack.me.wordsearchviewlibrary.presenter.WordSearchPresenter;

/**
 * Created by zjchai on 2016/12/11.
 */

public class WordSearchView extends RelativeLayout implements WordSearchContract.IWordSearchView {

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
    }

    private void init(){
        inflate(getContext(), R.layout.view_word_search,this);
        presenter = new WordSearchPresenter(this);
    }

    public WordSearchView search(String word){
        presenter.search(word);
        return this;
    }



}
