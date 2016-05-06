package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.ImageTypeJsonBean;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface MainViewModelImpl extends BaseModelImpl {
    void getImageTabs(String url, Observer<ImageTypeJsonBean> os);
}
