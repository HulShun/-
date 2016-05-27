package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.model.MainViewModel;
import com.example.newsclient.MyApplication;
import com.example.newsclient.R;
import com.example.newsclient.view.utils.AppUtil;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LaunchAcitivity extends AppCompatActivity {

    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        gifImageView = (GifImageView) findViewById(R.id.activity_launch_gif);
        ((GifDrawable) gifImageView.getDrawable()).addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationCompleted(int loopNumber) {
                final Intent intent = new Intent(LaunchAcitivity.this, MainActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        LaunchAcitivity.this.finish();
                    }
                }, 500);
            }
        });


        MainViewModel model = new MainViewModel();
        if (AppUtil.getInstance().isNetWorkConnected()) {
            //加载各个模块的标签页tab
            model.getImageTabsFromNet(Configuration.IMAGE_TYPE_URL);
            model.getVideoTabsFromNet(Configuration.YOUKU_API_BASE_URL);
        }

        String userType = AppUtil.getInstance().getFromShareRefence("login");
        MyApplication.getInstance().setUserTpye(userType);
    }
}
