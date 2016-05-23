package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.image.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface MainViewModelImpl extends BaseModelImpl {
    void getImageTabsFromNet(String url);

    void getImageTabsFromLocal(Observer<ImageTypeJsonBean> os);

    void getVideoTabsFromNet(String url);

    void getVideoTabsFromLocal(Observer<VideoTypeBean> os);
}
