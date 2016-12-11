package task.jack.me.wordsearchviewlibrary.contract;

/**
 * Created by zjchai on 2016/12/11.
 */
public class WordSearchContract {

    public interface IWordSearchView {

    }

    public interface IWordSearchPresenter {

        void search(String word);
    }

}