package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.video.VideoListBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface VideosModelImpl extends BaseModelImpl {

    void getVideos(Map<String, String> params, Observer<VideoListBean> os);
}
