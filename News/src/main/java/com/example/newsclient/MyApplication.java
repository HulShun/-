package com.example.newsclient;

import android.app.Activity;
import android.app.Application;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.utils.MyImageLoader;
import com.squareup.leakcanary.LeakCanary;
import com.youku.player.YoukuPlayerBaseConfiguration;

/**
 * Created by Administrator on 2016-04-11.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    public static YoukuPlayerBaseConfiguration configuration;


    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MyImageLoader.newInstance().init(this);
        LogUtil.setIsLog(true);
        LeakCanary.install(this);
        //Picasso.with(this).setIndicatorsEnabled(true); //显示图片来源——本地，内存，网络

        //优酷播放器初始化
        configuration = new YoukuPlayerBaseConfiguration(this) {
            /**
             * 通过覆写该方法，返回“正在缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的缓存界面
             */
            @Override
            public Class<? extends Activity> getCachingActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            /**
             * 通过覆写该方法，返回“已经缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的已缓存界面
             */

            @Override
            public Class<? extends Activity> getCachedActivityClass() {
                // TODO Auto-generated method stub
                return null;
            }

            /**
             * 配置视频的缓存路径，格式举例： /appname/videocache/
             * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
             *
             */
            @Override
            public String configDownloadPath() {
                // TODO Auto-generated method stub
                //return "/myapp/videocache/";			//举例
                return null;
            }
        };
    }
}
