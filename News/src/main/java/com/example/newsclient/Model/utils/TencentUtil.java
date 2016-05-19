package com.example.newsclient.Model.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.newsclient.Model.bean.TencentOpenBean;

/**
 * Created by Administrator on 2016-05-19.
 */
public class TencentUtil {

    public static void saveToken(Context c, TencentOpenBean data) {
        SharedPreferences sp = c.getSharedPreferences("qq", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("openid", data.getOpenid());
        editor.putString("access_token", data.getAccess_token());
        editor.putString("pay_token", data.getPay_token());
        editor.putInt("expires_in", data.getExpires_in());
        editor.putString("pfkey", data.getPfkey());

        editor.apply();
    }

    public static TencentOpenBean getToken(Context c) {
        SharedPreferences sp = c.getSharedPreferences("qq", Context.MODE_PRIVATE);
        TencentOpenBean data = new TencentOpenBean();
        data.setOpenid(sp.getString("openid", ""));
        data.setAccess_token(sp.getString("access_token", ""));
        data.setPay_token(sp.getString("pay_token", ""));
        data.setExpires_in(sp.getInt("expires_in", 0));
        data.setPfkey(sp.getString("pfkey", ""));

        return data;
    }
}
