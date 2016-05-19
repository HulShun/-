package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.bean.video.RecommendJsonVideoBean;

/**
 * Created by Administrator on 2016-05-17.
 */
public interface IVideoBriefViewImpl extends IBaseViewImpl {
    void onVideoDataResult(VideoItemBean data);

    void onRecommendVideoResult(RecommendJsonVideoBean data);
}
