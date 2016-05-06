package com.example.newsclient;

import android.app.Application;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.utils.MyImageLoader;

/**
 * Created by Administrator on 2016-04-11.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MyImageLoader.newInstance().init(this);
        LogUtil.setIsLog(true);
    }
}
