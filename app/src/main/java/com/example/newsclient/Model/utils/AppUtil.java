package com.example.newsclient.Model.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.example.newsclient.MyApplication;

/**
 * Created by Administrator on 2016-04-11.
 */
public class AppUtil {
    private Context context;
    private static AppUtil appUtil;

    public static AppUtil getInstance() {
        if (appUtil == null)
            synchronized (AppUtil.class) {
                if (appUtil == null) {
                    appUtil = new AppUtil();
                }
            }
        return appUtil;
    }

    private AppUtil() {
        context = MyApplication.getInstance().getApplicationContext();
    }

    public boolean checkExternalStorageState() {
        if (Environment.getExternalStorageState().endsWith(Environment.MEDIA_MOUNTED)
                || !(Environment.isExternalStorageRemovable())) {
            return true;
        } else {
            return false;
        }
    }

    public int getAppVersion() {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        return info.versionCode;
    }

    public boolean isNetWorkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkInfo network = connectivityManager.getActiveNetworkInfo();
            if (network == null || !network.isAvailable()) {
                return false;
            } else if (network.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }

        } else {
            NetworkInfo[] networks = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info : networks) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public int getScreenWidth() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public float getDensity() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

}
