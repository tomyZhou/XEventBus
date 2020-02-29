package com.example.xeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eventbus.EventBus;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

    }

    public void postMeesage(View view) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.message = "通知前面的两个页面";
        EventBus.getDefault().postEvent(messageEvent);

        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.count = 3;
        EventBus.getDefault().postEvent(noticeMessage);
    }
}
