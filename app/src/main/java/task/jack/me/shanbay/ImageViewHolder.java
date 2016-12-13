package task.jack.me.shanbay;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import task.jack.me.shanbay.adapter.TargetAdapter;
import task.jack.me.shanbay.manager.FileManager;
import task.jack.me.shanbay.model.ImageModel;
import task.jack.me.shanbay.utils.BitmapUtils;
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
        long start = System.currentTimeMillis();
        if (target != null) {
            Picasso.with(itemView.getContext()).cancelRequest(target);
            target = null;
        }
        imageView.setImageBitmap(null);
        if (image.isRefresh()) {
            refreshImage(image.getUrl());
        } else {
            loadImage(image.getUrl());
        }
        Log.d(TAG, "bind: use time:" + (System.currentTimeMillis() - start));
    }

    /**
     * 正常加载图片
     *
     * @param url 图片的网络地址，如果失败会用到
     */
    private void loadImage(String url) {
        Picasso.with(imageView.getContext())
                .load(getPicassoLocalPath(url))                                       // 从本地文件加载图片
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(360, 640)
                .into(target = getLoadLocalImageTarget(url));
    }


    /**
     * 正常情况下加载本地图片的回调Target
     *
     * @param url 要加载的网络图片的地址，在加载本地图片失败的时候会用到
     * @return target
     */
    private TargetAdapter getLoadLocalImageTarget(String url) {
        return TargetAdapter.builder()
                .onBitmapLoaded((bitmap, from) -> {                    // 加载成功
                    Log.d(TAG, "getLoadLocalImageTarget: ### success url:" + getBitmapLocalPath(url));
                    imageView.setImageBitmap(bitmap);
                })
                .onBitmapFailed(errorDrawable -> {
                    Log.d(TAG, "getLoadLocalImageTarget: ### fail url:" + getBitmapLocalPath(url));
                    Picasso.with(itemView.getContext())
                            .load(url)                             // 从网络加载图片
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .resize(360, 640)
                            .into(target = getLoadNetworkImageTarget(url));
                })
                .build();
    }

    /**
     * 正常加载本地图片失败之后，然后加载网络图片的回调
     *
     * @param url 网络图片地址
     * @return target实例
     */
    private TargetAdapter getLoadNetworkImageTarget(String url) {
        return TargetAdapter.builder()
                .onBitmapLoaded((bitmap, from) -> {
                    Log.d(TAG, "getLoadNetworkImageTarget: ### success url:" + url);
                    Schedulers.io()
                            .createWorker()
                            .schedule(() -> saveBitmap(bitmap, url));
                    imageView.setImageBitmap(bitmap);
                })
                .onBitmapFailed(errorDrawable -> {
                    Log.d(TAG, "getLoadNetworkImageTarget: ### fail url:" + url);
                    Observable
                            .create((Observable.OnSubscribe<Bitmap>) subscriber -> {
                                Bitmap bitmap = BitmapUtils.loadBitmap(itemView.getContext(), R.drawable.default_img, 360, 640);
                                if (bitmap != null) {
                                    subscriber.onNext(bitmap);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imageView.setImageBitmap(bitmap);
                            });
                }).build();
    }


    private void refreshImage(String url) {
        Picasso.with(imageView.getContext())
                .load(url)                                       // 从网络获取图片
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(360, 640)
                .into(target = TargetAdapter.builder()
                        .onBitmapLoaded((bitmap, from) -> {
                            Schedulers.io()
                                    .createWorker()
                                    .schedule(() -> saveBitmap(bitmap, url));
                            imageView.setImageBitmap(bitmap);
                        }).onBitmapFailed(errorDrawable ->
                                Picasso.with(itemView.getContext())
                                        .load(getBitmapLocalPath(url))               // 从本地加载图片
                                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                        .resize(360, 640)
                                        .error(R.drawable.default_img)         // 加载失败时的默认图
                                        .into(imageView))
                        .build()
                );
    }

    @NonNull
    private String getPicassoLocalPath(String url) {
        return "file:" + getBitmapLocalPath(url);
    }

    private void saveBitmap(Bitmap bitmap, String url) {
        FileUtils.saveBitmap(bitmap, getBitmapLocalPath(url), CompressFormat.JPEG);
    }

    private String getBitmapLocalPath(String url) {
        String name = FileUtils.getBitmapFullNameFromURL(url);
        return FileManager.getImageLocalPath(itemView.getContext(), name);
    }
}
