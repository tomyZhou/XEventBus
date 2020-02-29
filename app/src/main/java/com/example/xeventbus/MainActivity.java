package com.example.xeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.eventbus.EventBus;
import com.example.eventbus.OnEvent;
import com.example.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
    }

    @OnEvent(ThreadMode.MAIN_THREAD)
    public void getMessage(MessageEvent event) {
        Log.e(TAG, TAG + "收到了消息:" + event.message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void toOneActivity(View view) {
        Intent intent = new Intent(MainActivity.this, OneActivity.class);
        startActivity(intent);
    }
}
