package com.example.newsclient.Model.impl;

import android.app.Activity;

import com.example.newsclient.Model.bean.QQUserInfro;
import com.example.newsclient.Model.bean.WeiboUserInfo;
import com.example.newsclient.Model.bean.image.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.tencent.connect.UserInfo;

import org.json.JSONObject;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface MainViewModelImpl extends BaseModelImpl {
    void getImageTabsFromNet(String url);

    void getImageTabsFromLocal(Observer<ImageTypeJsonBean> os);

    void getVideoTabsFromNet(String url);

    void getVideoTabsFromLocal(Observer<VideoTypeBean> os);

    void saveQQInfo(JSONObject values, Activity context);

    void getQQUserInfo(UserInfo userInfo, Observer<QQUserInfro> observer);

    void getWeiboUserInfo(String token, String uid, Observer<WeiboUserInfo> observer);
}
