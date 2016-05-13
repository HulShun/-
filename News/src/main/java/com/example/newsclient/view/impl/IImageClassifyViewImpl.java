package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.image.ImageJsonBean;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface IImageClassifyViewImpl extends IBaseViewImpl {

    void onLoadData(ImageJsonBean data);

    void onCompleted();

    void showNoNetWork();


}
