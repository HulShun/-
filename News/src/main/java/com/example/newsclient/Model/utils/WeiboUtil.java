package com.example.newsclient.Model.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Administrator on 2016-05-19.
 */
public class WeiboUtil {
    public static void saveToken(Context c, Oauth2AccessToken data) {
        SharedPreferences sp = c.getSharedPreferences("weibo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("uid", data.getUid());
        editor.putString("token", data.getToken());
        editor.putString("expirestime", String.valueOf(data.getExpiresTime()));
        editor.putString("refresh_token", data.getRefreshToken());

        editor.apply();
    }

    public static Oauth2AccessToken getToken(Context c) {
        SharedPreferences sp = c.getSharedPreferences("qq", Context.MODE_PRIVATE);
        Oauth2AccessToken data = new Oauth2AccessToken();
        data.setUid(sp.getString("uid", ""));
        data.setToken(sp.getString("token", ""));
        data.setExpiresTime(Long.parseLong(sp.getString("expirestime", "")));
        data.setRefreshToken(sp.getString("refresh_token", ""));

        return data;
    }
}

