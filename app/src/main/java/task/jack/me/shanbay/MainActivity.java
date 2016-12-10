package task.jack.me.shanbay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_task_1, R.id.btn_task_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_task_1:
                Task1Activity.start(this);
                break;
            case R.id.btn_task_2:
                Task2Activity.start(this);
                break;
        }
    }
}
