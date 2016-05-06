package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.ImageMainTpyeBean;

import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface IMainViewImpl extends IBaseViewImpl {
    void onImageTabs(List<ImageMainTpyeBean> tabs);
    void onCompleted();

    void showNoNetWork();

}
