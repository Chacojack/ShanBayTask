package task.jack.me.shanbay;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import task.jack.me.shanbay.adapter.TargetAdapter;
import task.jack.me.shanbay.manager.FileManager;
import task.jack.me.shanbay.model.ImageModel;
import task.jack.me.shanbay.utils.BitmapUtils;
import task.jack.me.shanbay.utils.FileUtils;

/**
 * 使用RecyclerView显示图片的ViewHolder，其中包含了加载图片和刷新图片的主要逻辑：
 *
 * 正常的加载图片会使用：{@link #loadImage(String)}先从本地加载图片，
 * {@link #getLoadLocalImageTarget(String)}返回的Target中处理加载结果，如果成功，显示图片，如果失败则从
 * 网络加载，{@link #getLoadNetworkImageTarget(String)}返回的Target中处理网络加载的结果，如果成功，保存
 * 图片到本地{@link #saveBitmap(Bitmap, String)}并显示图片。如果失败加载默认图片。其中网络加载图片时的失败
 * 重试在Picasso中已经有了，会重试3次。
 *
 * 强制刷新：{@link #refreshImage(String)}会先从网络加载图片，{@link #getRefreshNetworkTarget(String)}
 * 得到的Target中处理加载网络图片的结果。如果加载成功，保存图片并显示，如果加载失败会加载本地图片，然后使用
 * {@link #getRefreshLocalTarget(String)}得到的Target，处理加载本地图片的结果，加载成功，显示图片，加载失败
 * 显示默认图片，网络加载的重试机制同上是使用Picasso中的。
 *
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
                .onBitmapLoaded((bitmap, from) -> {                    // 正常 加载本地图片成功
                    Log.d(TAG, "getLoadLocalImageTarget: ### success local path:" + getBitmapLocalPath(url));
                    imageView.setImageBitmap(bitmap);
                })
                .onBitmapFailed(errorDrawable -> {                     // 正常 加载本地图片失败
                    Log.d(TAG, "getLoadLocalImageTarget: ### fail local path:" + getBitmapLocalPath(url));
                    Picasso.with(itemView.getContext())
                            .load(url)                                 // 从网络加载图片
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
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
                .onBitmapLoaded((bitmap, from) -> {                // 正常加载时，通过网络加载图片成功
                    Log.d(TAG, "getLoadNetworkImageTarget: ### success url:" + url);
                    Schedulers.io().createWorker().schedule(() -> saveBitmap(bitmap, url));
                    imageView.setImageBitmap(bitmap);
                })
                .onBitmapFailed(errorDrawable -> {                 // 征程加载时，通过网络加载图片失败
                    Log.d(TAG, "getLoadNetworkImageTarget: ### fail url:" + url);
                    loadDefaultImage();
                }).build();
    }


    /**
     * 用于强制刷新的时候。
     *
     * @param url 图片网络地址
     */
    private void refreshImage(String url) {
        Picasso.with(imageView.getContext())
                .load(url)                                       // 从网络获取图片
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .resize(360, 640)
                .into(target = getRefreshNetworkTarget(url));
    }

    /**
     * 获得强制刷新时结果的回调
     *
     * @param url 图片网络地址
     * @return
     */
    private TargetAdapter getRefreshNetworkTarget(String url) {
        return TargetAdapter.builder()
                .onBitmapLoaded((bitmap, from) -> {                      // 强制刷新的时候，从网络加载图片成功
                    Log.d(TAG, "getRefreshNetworkTarget: ### success url:" + url);
                    Schedulers.io().createWorker().schedule(() -> saveBitmap(bitmap, url));
                    imageView.setImageBitmap(bitmap);
                }).onBitmapFailed(errorDrawable -> {                     // 强制刷新的时候，从网络加载图片失败
                    Log.d(TAG, "getRefreshNetworkTarget: ### fail url:" + url);
                    Picasso.with(itemView.getContext())
                            .load(getPicassoLocalPath(url))               // 从本地加载图片
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .resize(360, 640)
                            .into(target = getRefreshLocalTarget(url));
                })
                .build();
    }

    /**
     * 获得强制刷新时，从网络加载图片失败后，进行加载本地图片的结果回调
     *
     * @param url 图片网络地址
     * @return
     */
    private Target getRefreshLocalTarget(String url) {
        return TargetAdapter.builder()
                .onBitmapLoaded((bitmap, from) -> {              // 强制刷新的时候，从本地获取图片成功
                    Log.d(TAG, "getRefreshLocalTarget: ### success local path:" + getBitmapLocalPath(url));
                    imageView.setImageBitmap(bitmap);
                }).onBitmapFailed(errorDrawable -> {             // 强制刷新的时候，从本地获取图片失败
                    Log.d(TAG, "getRefreshLocalTarget: ### fail local path:" + getBitmapLocalPath(url));
                    loadDefaultImage();
                }).build();
    }

    /**
     * 加载默认图片
     */
    private void loadDefaultImage() {
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
