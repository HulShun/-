package com.example.newsclient.Model.impl;

import com.android.volley.toolbox.ImageLoader;
import com.example.newsclient.Model.bean.NewsListBean;

import java.util.Map;

import rx.Observer;

/**
 * Created by Administrator on 2016-04-11.
 */
public interface NewsFragmentModelImpl extends BaseModelImpl {
    void loadNewsList(int mode, Map<String, String> params, Observer<NewsListBean> os);

    void getBitmap(String url, ImageLoader.ImageListener listener);

    void toDataBase(Map<String, String> params, NewsListBean body);

}
