package com.example.newsclient.Model.impl;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.bean.ImageJsonBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface ImageClassifyModelImpl extends BaseModelImpl {
    void onLoadBitmap(String url, ImageLoader.ImageListener l);

    void getImageDatas(Map<String, String> params, Observer<ImageJsonBean> os);
}
