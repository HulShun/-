package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.VideosInFormBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-09.
 */
public interface VideoClassifyModelImpl extends BaseModelImpl {
    void loadVideosInform(Map<String, String> map, Observer<VideosInFormBean> os);
}
