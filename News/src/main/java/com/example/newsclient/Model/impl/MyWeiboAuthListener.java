package com.example.newsclient.Model.impl;

import android.os.Bundle;

import com.example.newsclient.Model.LogUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by Administrator on 2016-05-19.
 */
public class MyWeiboAuthListener implements com.sina.weibo.sdk.auth.WeiboAuthListener {
    @Override
    public void onComplete(Bundle bundle) {
        //// 从 Bundle 中解析 Token
        LogUtil.d("weibo", "微博登陆成功");
        Oauth2AccessToken mToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mToken.isSessionValid()) {
            doComplete(mToken);
        } else {
            LogUtil.d("weibo", bundle.getString("code"));
        }

    }

    protected void doComplete(Oauth2AccessToken oauth2AccessToken) {

    }

    @Override
    public void onWeiboException(WeiboException e) {
        LogUtil.d("onWeiboException", e.getMessage());
    }

    @Override
    public void onCancel() {

    }
}
