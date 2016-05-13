package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.ImageJsonBean;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface IImageListViewImpl extends IBaseViewImpl {
    void onRefreshed(ImageJsonBean data);

    void onLoadMore(ImageJsonBean data);

    void onCompleted();

    void showNoNetWork();


}
