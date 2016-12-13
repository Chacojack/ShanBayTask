package task.jack.me.shanbay.function;

import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

/**
 * Created by zjchai on 2016/12/13.
 */
public interface OnBitmapLoaded {

    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from);
}
