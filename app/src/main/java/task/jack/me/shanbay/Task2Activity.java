package task.jack.me.shanbay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import task.jack.me.shanbay.model.ImageModel;

/**
 * 1. 用列表的形式展示一组初始图片
 * 2. 图片显示的逻辑是
 * a. 先显示本地图片
 * b. 本地没有图片时，根据相应的url下载图片，下载完毕后，显示已下载的图片，
 * 并且保存在本地。在外部存储上的私有存储目录中创建一个Images文件夹，用来
 * 存放本地图片。(目录:SD卡目录/Android/data/包名/Images )
 * c. 无网络或者下载失败时，显示默认图片(res文件)
 * d. 图片下载失败时，增加合理的重试机制
 * 3. 有按钮可以用来把当前的一组图片更新成新的一组图片
 * a. 更新时，优先获取网络图片，并且同样的需要保存在本地(有本地图片，则进行 覆盖)
 * b. 无网络或者下载失败时，显示本地图片
 * c. 如果没有本地图片，显示默认图片
 * d. 图片下载失败时，增加合理的重试机制
 * 4. 合理的注释方便代码阅读
 */
public class Task2Activity extends AppCompatActivity {

    @BindView(R.id.img_list)
    RecyclerView imgList;
    @BindView(R.id.activity_task2)
    RelativeLayout activityTask2;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private String[] imgUrlArray1;
    private String[] imgUrlArray2;

    private ImageAdapter adapter;


    public static void start(Context context) {
        Intent intent = new Intent(context, Task2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);
        ButterKnife.bind(this);
        init();
        afterViews();
    }

    private void init() {
        imgUrlArray1 = getResources().getStringArray(R.array.img_list_1);
        imgUrlArray2 = getResources().getStringArray(R.array.img_list_2);
    }

    private void afterViews() {
        imgList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgList.setAdapter(adapter = new ImageAdapter());
        Observable
                .from(imgUrlArray1)
                .map(s -> ImageModel.builder().url(s).refresh(false).build())
                .forEach(
                        imageModel -> adapter.addData(imageModel),
                        throwable -> {
                        },
                        () -> adapter.notifyDataSetChanged());
    }


    @OnClick(R.id.btn_refresh)
    public void onClick() {
        btnRefresh.setEnabled(false);
        adapter.clear();
        Observable
                .from(imgUrlArray2)
                .map(s -> ImageModel.builder().url(s).refresh(true).build())
                .forEach(
                        imageModel -> adapter.addData(imageModel),
                        throwable -> {
                        },
                        () -> adapter.notifyDataSetChanged());
    }
}
























