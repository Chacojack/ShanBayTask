package task.jack.me.shanbay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 完成作业一的Activity
 *
 * 要求：
 *      1. 渲染一段英文文本
 *      2. 文本中的单词，被点击后可以高亮
 *      3. 单词高亮后，显示一个查词框(不限查词框形式)。 查词框的内容:
 *      a. 单词文本 b. 单词释义 c. 单词音标
 *      d. 点击可以播放单词发音 请通过扇贝网的开放API获取这些内容点击查看。
 *      4. 把这个查词框做成一个独立的控件(合理封装，接口友好，应用多种场景)
 *      5. 可以参考扇贝新闻的效果
 *      6. 合理的注释方便代码阅读
 *
 * 进阶：
 *      1. 通过自定义View实现该控件
 *      2. 文本实现两边对齐并且同样支持点击查词
 *      3. 给查词框增加出现和消失的动画或者合理的手势操作
 *
 */
public class Task1Activity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, Task1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);



    }
}
