package task.jack.me.wordsearchviewlibrary.presenter;

import android.util.Log;

import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.manager.BackgroundExecutor;
import task.jack.me.wordsearchviewlibrary.manager.HttpManager;
import task.jack.me.wordsearchviewlibrary.manager.UiThreadExecutor;
import task.jack.me.wordsearchviewlibrary.manager.protocol.ProtocolConstant;
import task.jack.me.wordsearchviewlibrary.manager.protocol.WordSearchResult;
import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;

/**
 * Created by v5_auto_coder on 2016/12/11
 */

public class WordSearchPresenter implements WordSearchContract.IWordSearchPresenter {

    private static final String TAG = WordSearchPresenter.class.getSimpleName();

    WordSearchContract.IWordSearchView view;

    public WordSearchPresenter(WordSearchContract.IWordSearchView v) {
        view = v;
    }


    @Override
    public void search(final String word) {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                WordSearchResult result = HttpManager.searchWord(word);
                if (result.getStatusCode() == ProtocolConstant.CODE_SUCCESS
                        && ProtocolConstant.MSG_SUCCESS.equals(result.getMsg())) {
                    WordSearchInfo data = result.getData();
                    if (data != null) {
                        Log.d(TAG, "run: data" + data.toJson());
                    } else {
                        Log.d(TAG, "run: data is null");
                    }
                }
            }
        });
    }
}