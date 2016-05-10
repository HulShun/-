package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.R;

public class LaunchAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        final Intent intent = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                LaunchAcitivity.this.finish();
            }
        }, 2000);

        MainViewModel model = new MainViewModel();
        if (AppUtil.getInstance().isNetWorkConnected()) {
            //加载各个模块的标签页tab
            model.getImageTabsFromNet(Configuration.IMAGE_TYPE_URL);
            model.getVideoTabsFromNet(Configuration.VIDEO_TYPE_URL);
        }

    }
}