package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.VideoItemBean;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public interface VideoItemModelImpl extends BaseModelImpl {
    void loadVideoItemData(String id, Observer<VideoItemBean> os);

    void loadVideoM8u3(String s, Observer<String> os);
}
