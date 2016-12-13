package task.jack.me.shanbay.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import lombok.Builder;
import lombok.Data;
import task.jack.me.shanbay.function.OnBitmapFailed;
import task.jack.me.shanbay.function.OnBitmapLoaded;
import task.jack.me.shanbay.function.OnPrepareLoad;

/**
 * Created by zjchai on 2016/12/13.
 */
@Data
@Builder
public class TargetAdapter implements Target {

    private static final String TAG = TargetAdapter.class.getSimpleName();

    OnBitmapLoaded onBitmapLoaded;
    OnBitmapFailed onBitmapFailed;
    OnPrepareLoad onPrepareLoad;

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Log.d(TAG, "onBitmapLoaded() called with: bitmap = [" + bitmap + "], from = [" + from + "]");
        if (onBitmapLoaded != null) {
            onBitmapLoaded.onBitmapLoaded(bitmap, from);
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.d(TAG, "onBitmapFailed() called with: errorDrawable = [" + errorDrawable + "]");
        if (onBitmapFailed != null) {
            onBitmapFailed.onBitmapFailed(errorDrawable);
        }
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.d(TAG, "onPrepareLoad() called with: placeHolderDrawable = [" + placeHolderDrawable + "]");
        if (onPrepareLoad != null) {
            onPrepareLoad.onPrepareLoad(placeHolderDrawable);
        }
    }
}
