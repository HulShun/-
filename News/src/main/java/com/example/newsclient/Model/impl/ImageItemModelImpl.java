package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.image.ImageJsonBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016/5/22.
 */
public interface ImageItemModelImpl extends BaseModelImpl {

    void getImageDatas(Map<String, String> parmas, Observer<ImageJsonBean> os);
}
