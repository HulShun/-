package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.QQUserInfro;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;

import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public interface IMainViewImpl extends IBaseViewImpl {
    void onImageTabs(List<ImageMainTypeBean> tabs);

    void onVideoTabs(List<VideoTypeBean.VideoCategoriesBean> tabs);

    void onNewsTabs(List<String> tabs);


    void onQQUserInfoResult(QQUserInfro qqUserInfro);
}
