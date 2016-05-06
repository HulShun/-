package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.ImageMainTypeBean;

import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface IMainViewImpl extends IBaseViewImpl {
    void onImageTabs(List<ImageMainTypeBean> tabs);
    void onCompleted();

    void showNoNetWork();

}
