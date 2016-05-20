package com.example.newsclient.view.impl;

import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.sina.weibo.sdk.api.VideoObject;

/**
 * Created by Administrator on 2016-05-11.
 */
public interface IVideoItemViewImpl extends IBaseViewImpl {

    void loadVideoItemInform(VideoItemBean data);

    void readyShareToWeibo(VideoObject videoObject);
}
