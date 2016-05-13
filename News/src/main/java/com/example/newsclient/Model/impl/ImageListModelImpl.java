package com.example.newsclient.Model.impl;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.bean.ImageJsonBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface ImageListModelImpl extends BaseModelImpl {

    void getImageDatas(Map<String, String> parmas, Observer<ImageJsonBean> os);

    void getBitmap(String url, ImageLoader.ImageListener l);
}
