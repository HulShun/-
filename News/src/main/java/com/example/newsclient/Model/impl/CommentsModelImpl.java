package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-16.
 */
public interface CommentsModelImpl extends BaseModelImpl {

    void loadCommentsFromNet(Map<String, String> params, Observer<Commentsv2JsonBean> observer);
}
