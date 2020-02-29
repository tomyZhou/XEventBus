package com.example.xeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.eventbus.EventBus;
import com.example.eventbus.OnEvent;
import com.example.eventbus.ThreadMode;

public class OneActivity extends AppCompatActivity {

    private String TAG = OneActivity.class.getSimpleName();
    private TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        tv_count = findViewById(R.id.tv_count);
        EventBus.getDefault().register(this);
    }

    @OnEvent(ThreadMode.MAIN_THREAD)
    public void getMessage(NoticeMessage event) {
        Log.e(TAG, TAG + "收到了消息:" + event.count);

        tv_count.setText("消息数量 :" + event.count);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void toTwoActivity(View view) {
        Intent intent = new Intent(OneActivity.this, TwoActivity.class);
        startActivity(intent);
    }
}
