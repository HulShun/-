package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.VideosInFormBean;

/**
 * Created by Administrator on 2016-05-09.
 */
public interface IVideoClassifyViewIpml extends IBaseViewImpl {
    void onUpdateVideos(VideosInFormBean data);

    void onloadMoreVideos(VideosInFormBean date);
    void onCompleted();
}
