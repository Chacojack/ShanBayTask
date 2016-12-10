package task.jack.me.shanbay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Task2Activity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, Task2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);



    }
}
