package task.jack.me.shanbay;

import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import rx.schedulers.Schedulers;
import task.jack.me.shanbay.adapter.TargetAdapter;
import task.jack.me.shanbay.model.ImageModel;
import task.jack.me.shanbay.utils.FileUtils;

/**
 * Created by zjchai on 2016/12/12.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ImageViewHolder.class.getSimpleName();

    private ImageView imageView;
    private Target target;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView;
    }

    public void bind(ImageModel image) {
        if (image.isRefresh()) {
            refreshImageFromNetWork(image.getUrl());
        } else {
            loadImage(image.getUrl());
        }
    }

    private void loadImage(String url) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("loadImage: url:%s, open indicator.",url));
            Picasso.with(imageView.getContext()).setIndicatorsEnabled(true);
        }
        Picasso.with(imageView.getContext())
                .load(getLocalPath(url))                                       // 从本地文件加载图片
                .skipMemoryCache()                                             // 绕过内存缓存
                .into(target = TargetAdapter.builder()
                        .onBitmapLoaded((bitmap, from) -> {                    // 加载成功
                            Schedulers.io()
                                    .createWorker()
                                    .schedule(() -> FileUtils.saveBitmap(bitmap, getLocalPath(url), CompressFormat.JPEG));
                            imageView.setImageBitmap(bitmap);
                        })
                        .onBitmapFailed(errorDrawable ->                       // 加载失败
                                Picasso.with(itemView.getContext())
                                        .load(url)                             // 从网络加载图片
                                        .skipMemoryCache()                     // 跳过内存缓存
                                        .error(R.drawable.default_img)         // 加载失败时的默认图
                                        .into(imageView))
                        .build()
                );
    }

    private void refreshImageFromNetWork(String url) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("loadImage: url:%s, open indicator.",url));
            Picasso.with(imageView.getContext()).setIndicatorsEnabled(true);
        }
        Picasso.with(imageView.getContext())
                .load(url)                                       // 从网络获取图片
                .skipMemoryCache()                               // 绕过内存缓存
                .into(target = TargetAdapter.builder()
                        .onBitmapLoaded((bitmap, from) -> {
                            Schedulers.io()
                                    .createWorker()
                                    .schedule(() -> FileUtils.saveBitmap(bitmap, getLocalPath(url), CompressFormat.JPEG));
                            imageView.setImageBitmap(bitmap);
                        }).onBitmapFailed(errorDrawable ->
                                Picasso.with(itemView.getContext())
                                        .load(getLocalPath(url))               // 从本地加载图片
                                        .skipMemoryCache()                     // 跳过内存缓存
                                        .error(R.drawable.default_img)         // 加载失败时的默认图
                                        .into(imageView))
                        .build()
                );
    }

    @NonNull
    private String getLocalPath(String url) {
        return Environment.getExternalStorageDirectory() + "/Images/" + FileUtils.getBitmapFullNameFromURL(url);
    }

}
