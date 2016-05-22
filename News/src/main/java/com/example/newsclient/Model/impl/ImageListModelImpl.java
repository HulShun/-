package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.image.ImageJsonBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface ImageListModelImpl extends BaseModelImpl {

    void getImageDatas(Map<String, Integer> parmas, Observer<ImageJsonBean> os);

}
