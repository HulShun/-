package com.example.newsclient.view.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.view.Constant;

public class NetWorkBroadcastReceiver extends BroadcastReceiver {

    private Handler mHandler;

    public NetWorkBroadcastReceiver(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppUtil.getInstance().isNetWorkConnected()) {
            mHandler.sendEmptyMessage(Constant.UPDATE_NETWORK);
        } else {
            mHandler.sendEmptyMessage(Constant.UPDATE_NONETWORK);
        }
    }
}
