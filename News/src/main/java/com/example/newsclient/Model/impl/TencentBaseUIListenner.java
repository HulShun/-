package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.LogUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016-05-19.
 */
public class TencentBaseUIListenner implements IUiListener {

    @Override
    public void onComplete(Object o) {
        // LogUtil.d("onTencentComplete", o.toString());
        doComplete((JSONObject) o);
    }

    protected void doComplete(JSONObject values) {

    }

    @Override
    public void onError(UiError e) {
        LogUtil.d("onTencentError:", "code:" + e.errorCode + ", msg:"
                + e.errorMessage + ", detail:" + e.errorDetail);
    }

    @Override
    public void onCancel() {
        LogUtil.d("onTencentCancel", "");
    }
}

