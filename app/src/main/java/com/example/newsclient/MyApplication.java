package com.example.newsclient;

import android.app.Application;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.utils.MyImageLoader;
import com.ykcloud.sdk.openapi.YKAPIFactory;

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
        //优酷播放器SDK初始化
        YKAPIFactory.initSDK(this, Configuration.YOUKU_KEYWORD, Configuration.YOUKU_SECRET);
    }
}
