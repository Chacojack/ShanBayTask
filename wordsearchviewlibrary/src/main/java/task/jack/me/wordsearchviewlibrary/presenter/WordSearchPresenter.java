package task.jack.me.wordsearchviewlibrary.presenter;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract;
import task.jack.me.wordsearchviewlibrary.contract.WordSearchContract.IWordSearchView;
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

    private String word;
    private MediaPlayer mediaPlayer;

    private WeakReference<IWordSearchView> viewWeakReference;

    public WordSearchPresenter(IWordSearchView v) {
        viewWeakReference = new WeakReference<>(v);
    }

    @Override
    public void search(@NonNull String word) {
        if (word.equals(this.word)) {
            return;
        }
        this.word = word;
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final WordSearchResult result = HttpManager.searchWord(WordSearchPresenter.this.word);
                if (result != null
                        && result.getStatusCode() == ProtocolConstant.CODE_SUCCESS
                        && ProtocolConstant.MSG_SUCCESS.equals(result.getMsg())) {
                    final WordSearchInfo data = result.getData();
                    if (data != null) {
                        Log.d(TAG, "run: data" + data.toJson());
                        UiThreadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                IWordSearchView view = viewWeakReference.get();
                                if (view != null) {
                                    view.showWordSearchInfo(data);
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "run: data is null");
                    }
                } else {
                    Log.d(TAG, "search : result" + result);
                    UiThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            IWordSearchView view = viewWeakReference.get();
                            if (view != null) {
                                if (result != null) {
                                    view.alarmSearchFailMsg(result.getMsg());
                                } else {
                                    view.alarmSearchFailDefaultMsg();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void playAudio(@NonNull final String audioUrl) {
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cancelCurrentMedia();
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                            WordSearchPresenter.this.mediaPlayer = null;
                            resetViewAudioBtn();
                        }
                    });
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    resetViewAudioBtn();
                    Log.d(TAG, "playAudio has exception");
                    e.printStackTrace();
                }
            }
        });
    }

    private void resetViewAudioBtn() {
        UiThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final IWordSearchView view = viewWeakReference.get();
                if (view != null) {
                    view.resetAudioBtn();
                }
            }
        });
    }

    private void cancelCurrentMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}