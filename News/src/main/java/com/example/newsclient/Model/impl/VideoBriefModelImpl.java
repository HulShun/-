package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-17.
 */
public interface VideoBriefModelImpl extends BaseModelImpl {
    void loadVideoData(String vid, Observer<VideoItemBean> observer);

    void loadRecommendVideoData(String vid, Observer<RecommendJsonVideoBean> observer);
}
