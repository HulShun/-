package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.VideoListBean;

/**
 * Created by Administrator on 2016-05-04.
 */
public interface IVideoViewImpl extends IBaseViewImpl {

    void updateDatas(VideoListBean videos);

    void onRefreshComplete();
  //  void onCompleted();

    void showNoNetWork();


}
