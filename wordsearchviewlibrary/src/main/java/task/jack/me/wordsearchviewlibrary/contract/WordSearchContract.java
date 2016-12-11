package task.jack.me.wordsearchviewlibrary.contract;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import task.jack.me.wordsearchviewlibrary.model.WordSearchInfo;

/**
 * Created by zjchai on 2016/12/11.
 */
public class WordSearchContract {

    public interface IWordSearchView {

        void showWordSearchInfo(@NonNull WordSearchInfo data);
    }

    public interface IWordSearchPresenter {

        void search(@NonNull String word);
    }

}