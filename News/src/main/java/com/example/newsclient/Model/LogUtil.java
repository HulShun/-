package com.example.newsclient.Model;

import android.util.Log;

/**
 * Created by Administrator on 2016-04-15.
 */
public class LogUtil {
    public static final String TAG_DB = "DB";
    public static final String TAG_SRCOLL = "srcoll";

    public static boolean isLog;

    public static void setIsLog(boolean isLog) {
        LogUtil.isLog = isLog;
    }

    public static void d(String tag, String s) {
        if (isLog) {
            Log.d(tag, s);
        }
    }

    public static void i(String tag, String s) {
        if (isLog) {
            Log.i(tag, s);
        }
    }

    public static void v(String tag, String s) {
        if (isLog) {
            Log.v(tag, s);
        }
    }

    public static void w(String tag, String s) {
        if (isLog) {
            Log.w(tag, s);
        }
    }

    public static void e(String tag, String s) {
        if (isLog) {
            Log.e(tag, s);
        }
    }
}
