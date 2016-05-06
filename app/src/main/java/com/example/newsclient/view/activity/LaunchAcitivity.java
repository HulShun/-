package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.ImageTypeJsonBean;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.R;

import rx.Observer;

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


        if (AppUtil.getInstance().isNetWorkConnected()) {
            //加载各个模块的标签页tab
            new MainViewModel().getImageTabs(Configuration.IMAGE_TYPE_URL, new Observer<ImageTypeJsonBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ImageTypeJsonBean imageTypeJsonBean) {

                }
            });
        }


    }
}
